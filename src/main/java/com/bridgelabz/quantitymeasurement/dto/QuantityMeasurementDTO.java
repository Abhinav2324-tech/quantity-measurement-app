
package com.bridgelabz.quantitymeasurement.dto;

import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;

public record QuantityMeasurementDTO(

        String operation,
        QuantityDTO firstOperand,
        QuantityDTO secondOperand,
        QuantityDTO result,

        Boolean comparisonResult,
        Double divisionResult,

        boolean error,
        String errorMessage
) {

    public static QuantityMeasurementDTO fromEntity(
            QuantityMeasurementEntity entity) {

        return new QuantityMeasurementDTO(
                entity.getOperation(),
                entity.getFirstOperand(),
                entity.getSecondOperand(),
                entity.getResult(),
                entity.getComparisonResult(),
                entity.getDivisionResult(),
                entity.hasError(),
                entity.getErrorMessage());
    }
}
