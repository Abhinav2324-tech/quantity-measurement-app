
package com.bridgelabz.quantitymeasurement.repository;

import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;
import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.bridgelabz.quantitymeasurement.util.ConnectionPool;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementDatabaseRepositoryTest {

    private ConnectionPool pool;
    private QuantityMeasurementDatabaseRepository repository;

    @BeforeEach
    void setUp() {
        String databaseName =
                "uc16_" + UUID.randomUUID().toString()
                        .replace("-", "");

        String url = "jdbc:h2:mem:" + databaseName
                + ";DB_CLOSE_DELAY=-1";

        pool = new ConnectionPool(
                url, "sa", "", 3, 1, 10000);

        repository =
                new QuantityMeasurementDatabaseRepository(pool);
    }

    @AfterEach
    void tearDown() {
        repository.releaseResources();
    }

    private QuantityDTO length(double value, String unit) {
        return new QuantityDTO(value, "LENGTH", unit);
    }

    private QuantityMeasurementEntity addition() {
        return QuantityMeasurementEntity.quantityResult(
                "ADD",
                length(1.0, "FEET"),
                length(12.0, "INCHES"),
                length(2.0, "FEET"));
    }

    private QuantityMeasurementEntity comparison() {
        return QuantityMeasurementEntity.comparisonResult(
                length(1.0, "FEET"),
                length(12.0, "INCHES"),
                true);
    }

    @Test
    void testSaveMeasurement() {
        repository.save(addition());

        assertEquals(1, repository.getTotalCount());
    }

    @Test
    void testRetrieveAllMeasurements() {
        repository.save(addition());
        repository.save(comparison());

        assertEquals(2,
                repository.getAllMeasurements().size());
    }

    @Test
    void testRetrieveAdditionResult() {
        repository.save(addition());

        QuantityMeasurementEntity stored =
                repository.getAllMeasurements().get(0);

        assertEquals("ADD", stored.getOperation());
        assertEquals(2.0,
                stored.getResult().getValue());
        assertEquals("FEET",
                stored.getResult().getUnitName());
    }

    @Test
    void testRetrieveComparisonResult() {
        repository.save(comparison());

        QuantityMeasurementEntity stored =
                repository.getAllMeasurements().get(0);

        assertEquals(Boolean.TRUE,
                stored.getComparisonResult());
    }

    @Test
    void testFilterByOperation() {
        repository.save(addition());
        repository.save(comparison());

        assertEquals(1,
                repository.getMeasurementsByOperation("ADD").size());

        assertEquals(1,
                repository.getMeasurementsByOperation("COMPARE").size());
    }

    @Test
    void testFilterByMeasurementType() {
        repository.save(addition());

        assertEquals(1,
                repository.getMeasurementsByType("LENGTH").size());

        assertEquals(0,
                repository.getMeasurementsByType("WEIGHT").size());
    }

    @Test
    void testDeleteAllMeasurements() {
        repository.save(addition());
        repository.save(comparison());

        repository.deleteAll();

        assertEquals(0, repository.getTotalCount());
        assertTrue(repository.getAllMeasurements().isEmpty());
    }

    @Test
    void testSaveErrorEntity() {
        QuantityMeasurementEntity error =
                QuantityMeasurementEntity.error(
                        "DIVIDE",
                        length(10.0, "FEET"),
                        length(0.0, "FEET"),
                        "Cannot divide by zero");

        repository.save(error);

        QuantityMeasurementEntity stored =
                repository.getAllMeasurements().get(0);

        assertTrue(stored.hasError());
        assertEquals("Cannot divide by zero",
                stored.getErrorMessage());
    }

    @Test
    void testSQLInjectionPrevention() {
        repository.save(addition());

        assertTrue(repository.getMeasurementsByOperation(
                "ADD' OR '1'='1").isEmpty());

        assertEquals(1, repository.getTotalCount());
    }

    @Test
    void testNullEntityRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> repository.save(null));
    }

    @Test
    void testPoolStatisticsAvailable() {
        String statistics = repository.getPoolStatistics();

        assertNotNull(statistics);
        assertTrue(statistics.contains("Active:"));
    }

    @Test
    void testDatabaseTablesCreated() throws Exception {
        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(
                     "SELECT COUNT(*) FROM quantity_measurement_history")) {

            assertTrue(result.next());
            assertEquals(0, result.getLong(1));
        }
    }

    @Test
    void testHistoryEntryCreated() throws Exception {
        repository.save(addition());

        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(
                     "SELECT COUNT(*) FROM quantity_measurement_history")) {

            assertTrue(result.next());
            assertEquals(1, result.getLong(1));
        }
    }
}
