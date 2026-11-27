package com.bridgelabz.quantitymeasurement;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Represents an immutable length that can be compared or converted.
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

    public double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
    }

    /**
     * Converts a raw value between length units without applying equality rounding.
     *
     * @throws IllegalArgumentException if the value is not finite, a unit is
     *         null, or the result overflows or underflows the double range
     */
    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Length must be a finite number");
        }

        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target units are required");
        }

        if (source == target || value == 0.0) {
            return value;
        }

        double result = target.convertFromBaseUnit(
                source.convertToBaseUnit(BigDecimal.valueOf(value))).doubleValue();

        if (!Double.isFinite(result) || result == 0.0) {
            throw new IllegalArgumentException("Converted length is outside the double range");
        }

        return result;
    }

    /**
     * Returns a new length in the target unit without changing this object.
     */
    public QuantityLength convertTo(LengthUnit target) {
        return new QuantityLength(convert(value, unit, target), target);
    }
    /**
     * Adds another length and keeps this length's unit.
     */
    public QuantityLength add(QuantityLength other) {
        return add(this, other, unit);
    }

    /**
     * Adds two lengths and defaults to the first operand's unit.
     */
    public static QuantityLength add(QuantityLength first, QuantityLength second) {
        if (first == null) {
            throw new IllegalArgumentException("First length is required");
        }

        return first.add(second);
    }

    /**
     * Adds two lengths and returns the result in the requested unit.
     */
    public static QuantityLength add(
            QuantityLength first, QuantityLength second, LengthUnit targetUnit) {

        if (first == null || second == null) {
            throw new IllegalArgumentException("Both lengths are required");
        }

        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit is required");
        }

        BigDecimal sumInFeet = first.unit.convertToBaseUnit(BigDecimal.valueOf(first.value))
                .add(second.unit.convertToBaseUnit(BigDecimal.valueOf(second.value)));

        double result = targetUnit.convertFromBaseUnit(sumInFeet).doubleValue();

        if (!Double.isFinite(result)
                || (result == 0.0 && sumInFeet.signum() != 0)) {
            throw new IllegalArgumentException("Sum is outside the double range");
        }

        return new QuantityLength(result, targetUnit);
    }


    private BigDecimal comparisonKey() {
        return unit.toInches(BigDecimal.valueOf(value))
                .setScale(6, RoundingMode.HALF_UP)
                .stripTrailingZeros();
    }

    public boolean compare(QuantityLength other) {
        return other != null && comparisonKey().equals(other.comparisonKey());
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
        return comparisonKey().hashCode();
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }
}