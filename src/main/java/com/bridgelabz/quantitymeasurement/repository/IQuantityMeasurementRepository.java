
package com.bridgelabz.quantitymeasurement.repository;

import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.List;

/**
 * UC15: Data access contract for measurement history.
 */
public interface IQuantityMeasurementRepository {

    void save(QuantityMeasurementEntity entity);

    List<QuantityMeasurementEntity> getAllMeasurements();
}
