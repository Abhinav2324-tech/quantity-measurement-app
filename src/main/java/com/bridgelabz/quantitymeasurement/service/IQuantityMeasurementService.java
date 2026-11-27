
package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;
import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;

/**
 * UC15: Service contract for quantity operations.
 */
public interface IQuantityMeasurementService {

    QuantityMeasurementEntity compare(
            QuantityDTO first, QuantityDTO second);

    QuantityMeasurementEntity convert(
            QuantityDTO source, String targetUnit);

    QuantityMeasurementEntity add(
            QuantityDTO first,
            QuantityDTO second,
            String targetUnit);

    QuantityMeasurementEntity subtract(
            QuantityDTO first,
            QuantityDTO second,
            String targetUnit);

    QuantityMeasurementEntity divide(
            QuantityDTO first, QuantityDTO second);
}
