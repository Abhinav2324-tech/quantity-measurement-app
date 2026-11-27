
package com.bridgelabz.quantitymeasurement.repository;

import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;
import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.bridgelabz.quantitymeasurement.exception.DatabaseException;
import com.bridgelabz.quantitymeasurement.util.ConnectionPool;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UC16: JDBC implementation of measurement persistence.
 */
public final class QuantityMeasurementDatabaseRepository
        implements IQuantityMeasurementRepository {

    private final ConnectionPool pool;

    public QuantityMeasurementDatabaseRepository(
            ConnectionPool pool) {

        if (pool == null) {
            throw new IllegalArgumentException(
                    "ConnectionPool cannot be null");
        }

        this.pool = pool;
        initializeSchema();
    }

    /**
     * Creates database tables and indexes from schema.sql.
     */
    private void initializeSchema() {

        try (InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream("db/schema.sql")) {

            if (input == null) {
                throw new DatabaseException(
                        "Database schema.sql not found");
            }

            String schema = new String(
                    input.readAllBytes(),
                    StandardCharsets.UTF_8);

            try (Connection connection = pool.getConnection();
                 Statement statement = connection.createStatement()) {

                for (String sql : schema.split(";")) {
                    if (!sql.isBlank()) {
                        statement.execute(sql.trim());
                    }
                }
            }

        } catch (DatabaseException exception) {
            throw exception;

        } catch (Exception exception) {
            throw new DatabaseException(
                    "Failed to initialize database schema",
                    exception);
        }
    }

    /**
     * Sets nullable DTO fields in a prepared statement.
     */
    private void setQuantity(
            PreparedStatement statement,
            int startIndex,
            QuantityDTO quantity) throws SQLException {

        if (quantity == null) {
            statement.setNull(startIndex, Types.DOUBLE);
            statement.setNull(startIndex + 1, Types.VARCHAR);
            statement.setNull(startIndex + 2, Types.VARCHAR);
            return;
        }

        statement.setDouble(startIndex, quantity.getValue());
        statement.setString(
                startIndex + 1, quantity.getMeasurementType());
        statement.setString(
                startIndex + 2, quantity.getUnitName());
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {

        if (entity == null) {
            throw new IllegalArgumentException(
                    "Measurement entity cannot be null");
        }

        String insertMeasurement = """
                INSERT INTO quantity_measurement_entity (
                    operation_type,
                    first_value, first_measurement_type, first_unit,
                    second_value, second_measurement_type, second_unit,
                    result_value, result_measurement_type, result_unit,
                    comparison_result, division_result, error_message
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        String insertHistory = """
                INSERT INTO quantity_measurement_history
                    (measurement_id, operation_type)
                VALUES (?, ?)
                """;

        try (Connection connection = pool.getConnection()) {

            connection.setAutoCommit(false);

            try {
                long measurementId;

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     insertMeasurement,
                                     Statement.RETURN_GENERATED_KEYS)) {

                    statement.setString(1, entity.getOperation());

                    setQuantity(
                            statement, 2, entity.getFirstOperand());

                    setQuantity(
                            statement, 5, entity.getSecondOperand());

                    setQuantity(
                            statement, 8, entity.getResult());

                    if (entity.getComparisonResult() == null) {
                        statement.setNull(11, Types.BOOLEAN);
                    } else {
                        statement.setBoolean(
                                11, entity.getComparisonResult());
                    }

                    if (entity.getDivisionResult() == null) {
                        statement.setNull(12, Types.DOUBLE);
                    } else {
                        statement.setDouble(
                                12, entity.getDivisionResult());
                    }

                    statement.setString(
                            13, entity.getErrorMessage());

                    statement.executeUpdate();

                    try (ResultSet keys =
                                 statement.getGeneratedKeys()) {

                        if (!keys.next()) {
                            throw new SQLException(
                                    "No generated measurement ID");
                        }

                        measurementId = keys.getLong(1);
                    }
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(insertHistory)) {

                    statement.setLong(1, measurementId);
                    statement.setString(2, entity.getOperation());
                    statement.executeUpdate();
                }

                connection.commit();

            } catch (SQLException | RuntimeException exception) {

                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    exception.addSuppressed(rollbackException);
                }

                throw new DatabaseException(
                        "Unable to save measurement", exception);
            }

        } catch (SQLException exception) {
            throw new DatabaseException(
                    "Database connection failed during save",
                    exception);
        }
    }

    /**
     * Reconstructs a DTO from its database columns.
     */
    private QuantityDTO readQuantity(
            ResultSet result,
            String prefix) throws SQLException {

        Object storedValue =
                result.getObject(prefix + "_value");

        if (storedValue == null) {
            return null;
        }

        double value = ((Number) storedValue).doubleValue();

        return new QuantityDTO(
                value,
                result.getString(prefix + "_measurement_type"),
                result.getString(prefix + "_unit"));
    }

    /**
     * Maps a JDBC result row to an existing UC15 entity.
     */
    private QuantityMeasurementEntity mapEntity(
            ResultSet result) throws SQLException {

        String operation = result.getString("operation_type");

        QuantityDTO first =
                readQuantity(result, "first");

        QuantityDTO second =
                readQuantity(result, "second");

        QuantityDTO output =
                readQuantity(result, "result");

        String error = result.getString("error_message");

        if (error != null) {
            return QuantityMeasurementEntity.error(
                    operation, first, second, error);
        }

        if ("COMPARE".equals(operation)) {
            return QuantityMeasurementEntity.comparisonResult(
                    first,
                    second,
                    result.getBoolean("comparison_result"));
        }

        if ("DIVIDE".equals(operation)) {
            return QuantityMeasurementEntity.divisionResult(
                    first,
                    second,
                    result.getDouble("division_result"));
        }

        return QuantityMeasurementEntity.quantityResult(
                operation, first, second, output);
    }

    /**
     * Executes a SELECT statement with optional parameters.
     */
    private List<QuantityMeasurementEntity> query(
            String sql,
            String parameter) {

        List<QuantityMeasurementEntity> measurements =
                new ArrayList<>();

        try (Connection connection = pool.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            if (parameter != null) {
                statement.setString(1, parameter);
            }

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {
                    measurements.add(mapEntity(result));
                }
            }

            return measurements;

        } catch (SQLException exception) {
            throw new DatabaseException(
                    "Unable to retrieve measurements",
                    exception);
        }
    }

    @Override
    public List<QuantityMeasurementEntity>
    getAllMeasurements() {

        return query("""
                SELECT *
                FROM quantity_measurement_entity
                ORDER BY id
                """, null);
    }

    @Override
    public List<QuantityMeasurementEntity>
    getMeasurementsByOperation(String operation) {

        return query("""
                SELECT *
                FROM quantity_measurement_entity
                WHERE operation_type = ?
                ORDER BY id
                """, operation);
    }

    @Override
    public List<QuantityMeasurementEntity>
    getMeasurementsByType(String type) {

        return query("""
                SELECT *
                FROM quantity_measurement_entity
                WHERE first_measurement_type = ?
                ORDER BY id
                """, type);
    }

    @Override
    public long getTotalCount() {

        String sql = """
                SELECT COUNT(*)
                FROM quantity_measurement_entity
                """;

        try (Connection connection = pool.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            result.next();
            return result.getLong(1);

        } catch (SQLException exception) {
            throw new DatabaseException(
                    "Unable to count measurements",
                    exception);
        }
    }

    @Override
    public void deleteAll() {

        try (Connection connection = pool.getConnection()) {

            connection.setAutoCommit(false);

            try (Statement statement = connection.createStatement()) {

                statement.executeUpdate(
                        "DELETE FROM quantity_measurement_history");

                statement.executeUpdate(
                        "DELETE FROM quantity_measurement_entity");

                connection.commit();

            } catch (SQLException exception) {

                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    exception.addSuppressed(rollbackException);
                }

                throw new DatabaseException(
                        "Unable to delete measurements",
                        exception);
            }

        } catch (SQLException exception) {
            throw new DatabaseException(
                    "Database connection failed during delete",
                    exception);
        }
    }

    @Override
    public String getPoolStatistics() {
        return pool.getStatistics();
    }

    @Override
    public void releaseResources() {
        pool.close();
    }
}
