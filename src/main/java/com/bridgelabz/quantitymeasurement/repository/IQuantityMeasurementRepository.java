
package com.bridgelabz.quantitymeasurement.repository;

import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.List;

/**
 * UC16: Repository abstraction for both cache and JDBC storage.
 */
public interface IQuantityMeasurementRepository
        extends AutoCloseable {

    void save(QuantityMeasurementEntity entity);

    List<QuantityMeasurementEntity> getAllMeasurements();

    default List<QuantityMeasurementEntity>
    getMeasurementsByOperation(String operation) {

        return getAllMeasurements().stream()
                .filter(entity -> entity.getOperation()
                        .equalsIgnoreCase(operation))
                .toList();
    }

    default List<QuantityMeasurementEntity>
    getMeasurementsByType(String type) {

        return getAllMeasurements().stream()
                .filter(entity ->
                        entity.getFirstOperand() != null
                                && entity.getFirstOperand()
                                .getMeasurementType()
                                .equalsIgnoreCase(type))
                .toList();
    }

    default long getTotalCount() {
        return getAllMeasurements().size();
    }

    default void deleteAll() {
        throw new UnsupportedOperationException(
                "Delete not supported by this repository");
    }

    default String getPoolStatistics() {
        return "No connection pool";
    }

    default void releaseResources() {
        // Cache repositories may not own JDBC resources.
    }

    @Override
    default void close() {
        releaseResources();
    }
}
