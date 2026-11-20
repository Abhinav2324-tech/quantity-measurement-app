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

        double result = toBaseValue(value, source)
                .divide(BigDecimal.valueOf(target.getConversionFactor()),
                        MathContext.DECIMAL128)
                .doubleValue();

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
     * Adds another length and returns a new result in this length's unit.
     */
    public QuantityLength add(QuantityLength other) {
        if (other == null) {
            throw new IllegalArgumentException("Second length is required");
        }

        BigDecimal sumInInches = toBaseValue(value, unit)
                .add(toBaseValue(other.value, other.unit));

        double result = sumInInches
                .divide(BigDecimal.valueOf(unit.getConversionFactor()),
                        MathContext.DECIMAL128)
                .doubleValue();

        if (!Double.isFinite(result)
                || (result == 0.0 && sumInInches.signum() != 0)) {
            throw new IllegalArgumentException("Sum is outside the double range");
        }

        return new QuantityLength(result, unit);
    }

    /**
     * Adds two lengths and returns the result in the first length's unit.
     */
    public static QuantityLength add(QuantityLength first, QuantityLength second) {
        if (first == null) {
            throw new IllegalArgumentException("First length is required");
        }

        return first.add(second);
    }
    private static BigDecimal toBaseValue(double value, LengthUnit unit) {
        return BigDecimal.valueOf(value)
                .multiply(BigDecimal.valueOf(unit.getConversionFactor()));
    }

    private BigDecimal comparisonKey() {
        return toBaseValue(value, unit)
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