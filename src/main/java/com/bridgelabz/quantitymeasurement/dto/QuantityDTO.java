
package com.bridgelabz.quantitymeasurement.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * UC15: Data transfer object for quantity measurements.
 */
public final class QuantityDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final double value;
    private final String measurementType;
    private final String unitName;

    public QuantityDTO(
            double value,
            String measurementType,
            String unitName) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Quantity value must be finite");
        }

        if (measurementType == null ||
                measurementType.isBlank()) {
            throw new IllegalArgumentException(
                    "Measurement type cannot be empty");
        }

        if (unitName == null || unitName.isBlank()) {
            throw new IllegalArgumentException(
                    "Unit name cannot be empty");
        }

        this.value = value;
        this.measurementType =
                measurementType.trim().toUpperCase();
        this.unitName = unitName.trim().toUpperCase();
    }

    public double getValue() {
        return value;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public String getUnitName() {
        return unitName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof QuantityDTO other)) {
            return false;
        }

        return Double.compare(value, other.value) == 0
                && measurementType.equals(other.measurementType)
                && unitName.equals(other.unitName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                value, measurementType, unitName);
    }

    @Override
    public String toString() {
        return value + " " + unitName;
    }
}
