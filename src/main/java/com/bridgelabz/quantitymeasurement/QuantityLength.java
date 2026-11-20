package com.bridgelabz.quantitymeasurement;

import java.math.BigDecimal;

/**
 * Represents an immutable length and compares lengths in a common unit.
 */
public final class QuantityLength {
    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Length must be a finite number");
        }

        if (unit == null) {
            throw new IllegalArgumentException("Length unit is required");
        }

        this.value = value;
        this.unit = unit;
    }

    private BigDecimal convertToBaseUnit() {
        return BigDecimal.valueOf(value)
                .multiply(BigDecimal.valueOf(unit.getConversionFactor()))
                .stripTrailingZeros();
    }

    public boolean compare(QuantityLength other) {
        return other != null
                && convertToBaseUnit().equals(other.convertToBaseUnit());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof QuantityLength other)) {
            return false;
        }

        return compare(other);
    }

    @Override
    public int hashCode() {
        return convertToBaseUnit().hashCode();
    }
}