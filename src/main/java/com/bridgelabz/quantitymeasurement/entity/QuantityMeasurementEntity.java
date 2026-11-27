
package com.bridgelabz.quantitymeasurement.entity;

import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;

import java.io.Serializable;

/**
 * UC15: Stores a measurement operation and its result.
 */
public final class QuantityMeasurementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String operation;
    private final QuantityDTO firstOperand;
    private final QuantityDTO secondOperand;
    private final QuantityDTO result;
    private final Boolean comparisonResult;
    private final Double divisionResult;
    private final String errorMessage;

    private QuantityMeasurementEntity(
            String operation,
            QuantityDTO firstOperand,
            QuantityDTO secondOperand,
            QuantityDTO result,
            Boolean comparisonResult,
            Double divisionResult,
            String errorMessage) {

        this.operation = operation;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
        this.result = result;
        this.comparisonResult = comparisonResult;
        this.divisionResult = divisionResult;
        this.errorMessage = errorMessage;
    }

    public static QuantityMeasurementEntity quantityResult(
            String operation,
            QuantityDTO first,
            QuantityDTO second,
            QuantityDTO result) {

        return new QuantityMeasurementEntity(
                operation, first, second,
                result, null, null, null);
    }

    public static QuantityMeasurementEntity comparisonResult(
            QuantityDTO first,
            QuantityDTO second,
            boolean equal) {

        return new QuantityMeasurementEntity(
                "COMPARE", first, second,
                null, equal, null, null);
    }

    public static QuantityMeasurementEntity divisionResult(
            QuantityDTO first,
            QuantityDTO second,
            double ratio) {

        return new QuantityMeasurementEntity(
                "DIVIDE", first, second,
                null, null, ratio, null);
    }

    public static QuantityMeasurementEntity error(
            String operation,
            QuantityDTO first,
            QuantityDTO second,
            String message) {

        return new QuantityMeasurementEntity(
                operation, first, second,
                null, null, null, message);
    }

    public String getOperation() {
        return operation;
    }

    public QuantityDTO getFirstOperand() {
        return firstOperand;
    }

    public QuantityDTO getSecondOperand() {
        return secondOperand;
    }

    public QuantityDTO getResult() {
        return result;
    }

    public Boolean getComparisonResult() {
        return comparisonResult;
    }

    public Double getDivisionResult() {
        return divisionResult;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null;
    }

    @Override
    public String toString() {

        if (hasError()) {
            return operation + " ERROR: " + errorMessage;
        }

        if (comparisonResult != null) {
            return "COMPARE result: " + comparisonResult;
        }

        if (divisionResult != null) {
            return "DIVIDE result: " + divisionResult;
        }

        return operation + " result: " + result;
    }
}
