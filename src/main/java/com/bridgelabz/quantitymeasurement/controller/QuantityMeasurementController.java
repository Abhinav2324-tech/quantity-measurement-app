
package com.bridgelabz.quantitymeasurement.controller;

import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;
import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.bridgelabz.quantitymeasurement.service.IQuantityMeasurementService;

/**
 * UC15: Thin controller that delegates operations
 * to the service layer.
 */
public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(
            IQuantityMeasurementService service) {

        if (service == null) {
            throw new IllegalArgumentException(
                    "Measurement service cannot be null");
        }

        this.service = service;
    }

    public QuantityMeasurementEntity performComparison(
            QuantityDTO first,
            QuantityDTO second) {

        return service.compare(first, second);
    }

    public QuantityMeasurementEntity performConversion(
            QuantityDTO source,
            String targetUnit) {

        return service.convert(source, targetUnit);
    }

    public QuantityMeasurementEntity performAddition(
            QuantityDTO first,
            QuantityDTO second,
            String targetUnit) {

        return service.add(first, second, targetUnit);
    }

    public QuantityMeasurementEntity performSubtraction(
            QuantityDTO first,
            QuantityDTO second,
            String targetUnit) {

        return service.subtract(first, second, targetUnit);
    }

    public QuantityMeasurementEntity performDivision(
            QuantityDTO first,
            QuantityDTO second) {

        return service.divide(first, second);
    }

    public void displayResult(
            QuantityMeasurementEntity result) {

        System.out.println(result);
    }
}
