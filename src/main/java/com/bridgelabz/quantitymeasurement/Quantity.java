
package com.bridgelabz.quantitymeasurement;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * UC10: Generic quantity class supporting multiple
 * measurement categories using the IMeasurable interface.
 */
public final class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Quantity value must be finite");
        }

        if (unit == null) {
            throw new IllegalArgumentException(
                    "Measurement unit cannot be null");
        }

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    /**
     * Normalizes measurements for equality comparison.
     */
    private BigDecimal comparisonKey() {
        double baseValue = unit.convertToBaseUnit(value);

        if (!Double.isFinite(baseValue)) {
            throw new IllegalArgumentException(
                    "Base value is outside double range");
        }

        return BigDecimal.valueOf(baseValue)
                .setScale(4, RoundingMode.HALF_UP)
                .stripTrailingZeros();
    }

    /**
     * Compares two quantities of the same measurement category.
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Quantity<?> other)) {
            return false;
        }

        if (unit.getClass() != other.unit.getClass()) {
            return false;
        }

        return comparisonKey().equals(other.comparisonKey());
    }

    @Override
    public int hashCode() {
        return 31 * unit.getClass().hashCode()
                + comparisonKey().hashCode();
    }

    /**
     * Converts the quantity to the specified target unit.
     */
    public Quantity<U> convertTo(U targetUnit) {

        validateTargetUnit(targetUnit);

        double baseValue = unit.convertToBaseUnit(value);
        double convertedValue =
                targetUnit.convertFromBaseUnit(baseValue);

        return new Quantity<>(
                roundToTwoDecimals(convertedValue),
                targetUnit);
    }

    /**
     * Adds another quantity in the first operand's unit.
     */
    public Quantity<U> add(Quantity<U> other) {
        return add(other, unit);
    }

    /**
     * Adds another quantity in the specified target unit.
     */
    public Quantity<U> add(Quantity<U> other, U targetUnit) {

        if (other == null) {
            throw new IllegalArgumentException(
                    "Second quantity cannot be null");
        }

        validateTargetUnit(targetUnit);

        if (unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException(
                    "Cannot add different measurement categories");
        }

        double firstBase = unit.convertToBaseUnit(value);
        double secondBase =
                other.unit.convertToBaseUnit(other.value);

        double sumInBase = firstBase + secondBase;

        double result =
                targetUnit.convertFromBaseUnit(sumInBase);

        return new Quantity<>(
                roundToTwoDecimals(result),
                targetUnit);
    }

    /**
     * Ensures that the target unit belongs to the same category.
     */
    private void validateTargetUnit(U targetUnit) {

        if (targetUnit == null) {
            throw new IllegalArgumentException(
                    "Target unit cannot be null");
        }

        if (unit.getClass() != targetUnit.getClass()) {
            throw new IllegalArgumentException(
                    "Incompatible measurement units");
        }
    }

    private static double roundToTwoDecimals(double number) {

        if (!Double.isFinite(number)) {
            throw new IllegalArgumentException(
                    "Result is outside double range");
        }

        return BigDecimal.valueOf(number)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    @Override
    public String toString() {
        return value + " " + unit.getUnitName();
    }
}
