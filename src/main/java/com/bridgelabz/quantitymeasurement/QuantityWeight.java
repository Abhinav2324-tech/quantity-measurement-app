
package com.bridgelabz.quantitymeasurement;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * UC9: Supports weight equality, conversion, and addition.
 */
public final class QuantityWeight {

    private final double value;
    private final WeightUnit unit;

    public QuantityWeight(double value, WeightUnit unit) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Weight must be a finite number");
        }

        if (unit == null) {
            throw new IllegalArgumentException(
                    "Weight unit cannot be null");
        }

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public WeightUnit getUnit() {
        return unit;
    }

    // Convert to kilograms using precise decimal arithmetic
    private BigDecimal toBaseUnit() {
        return BigDecimal.valueOf(value)
                .multiply(BigDecimal.valueOf(
                        unit.getConversionFactor()));
    }

    // Normalize precision for equality and hashCode
    private BigDecimal comparisonKey() {
        return toBaseUnit()
                .setScale(4, RoundingMode.HALF_UP)
                .stripTrailingZeros();
    }

    /**
     * Compare two weights after conversion to kilograms.
     */
    public boolean compare(QuantityWeight other) {
        return other != null
                && comparisonKey().equals(other.comparisonKey());
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof QuantityWeight other)) {
            return false;
        }

        return compare(other);
    }

    @Override
    public int hashCode() {
        return comparisonKey().hashCode();
    }

    /**
     * Convert weight to another unit.
     */
    public QuantityWeight convertTo(WeightUnit targetUnit) {

        if (targetUnit == null) {
            throw new IllegalArgumentException(
                    "Target unit cannot be null");
        }

        if (targetUnit == unit) {
            return new QuantityWeight(value, unit);
        }

        double convertedValue = toBaseUnit()
                .divide(
                        BigDecimal.valueOf(
                                targetUnit.getConversionFactor()),
                        MathContext.DECIMAL128)
                .doubleValue();

        if (!Double.isFinite(convertedValue)) {
            throw new IllegalArgumentException(
                    "Converted weight is outside double range");
        }

        return new QuantityWeight(convertedValue, targetUnit);
    }

    /**
     * Add another weight using this object's unit.
     */
    public QuantityWeight add(QuantityWeight other) {
        return add(this, other, unit);
    }

    /**
     * Add two weights using the first operand's unit.
     */
    public static QuantityWeight add(
            QuantityWeight first,
            QuantityWeight second) {

        if (first == null) {
            throw new IllegalArgumentException(
                    "First weight cannot be null");
        }

        return add(first, second, first.unit);
    }

    /**
     * Add two weights using an explicitly specified target unit.
     */
    public static QuantityWeight add(
            QuantityWeight first,
            QuantityWeight second,
            WeightUnit targetUnit) {

        if (first == null || second == null) {
            throw new IllegalArgumentException(
                    "Both weights are required");
        }

        if (targetUnit == null) {
            throw new IllegalArgumentException(
                    "Target unit cannot be null");
        }

        BigDecimal totalInKilograms =
                first.toBaseUnit().add(second.toBaseUnit());

        double result = totalInKilograms
                .divide(
                        BigDecimal.valueOf(
                                targetUnit.getConversionFactor()),
                        MathContext.DECIMAL128)
                .doubleValue();

        if (!Double.isFinite(result)) {
            throw new IllegalArgumentException(
                    "Weight addition result is outside double range");
        }

        return new QuantityWeight(result, targetUnit);
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }
}
