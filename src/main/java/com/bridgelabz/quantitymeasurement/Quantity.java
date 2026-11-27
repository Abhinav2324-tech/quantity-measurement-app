
package com.bridgelabz.quantitymeasurement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.DoubleBinaryOperator;

/**
 * UC13: Generic Quantity class with centralized arithmetic logic.
 * Supports equality, conversion, addition, subtraction and division.
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

    /**
     * UC13: Arithmetic operations using lambda expressions.
     */
    private enum ArithmeticOperation {

        ADD((a, b) -> a + b),

        SUBTRACT((a, b) -> a - b),

        DIVIDE((a, b) -> {
            if (b == 0.0) {
                throw new ArithmeticException("Cannot divide by zero");
            }
            return a / b;
        });

        private final DoubleBinaryOperator operation;

        ArithmeticOperation(DoubleBinaryOperator operation) {
            this.operation = operation;
        }

        public double compute(double a, double b) {
            return operation.applyAsDouble(a, b);
        }
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    /**
     * Normalizes values for equality comparison.
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
     * Compares quantities belonging to the same category.
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
     * Converts a quantity to another compatible unit.
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
     * Validates the second operand and optional target unit.
     */
    private void validateArithmeticOperands(
            Quantity<U> other,
            U targetUnit,
            boolean targetUnitRequired) {

        if (other == null) {
            throw new IllegalArgumentException(
                    "Second quantity cannot be null");
        }

        if (unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException(
                    "Cannot perform arithmetic on different measurement categories");
        }

        if (!Double.isFinite(value)
                || !Double.isFinite(other.value)) {
            throw new IllegalArgumentException(
                    "Quantity values must be finite");
        }

        if (targetUnitRequired) {

            if (targetUnit == null) {
                throw new IllegalArgumentException(
                        "Target unit cannot be null");
            }

            if (unit.getClass() != targetUnit.getClass()) {
                throw new IllegalArgumentException(
                        "Target unit belongs to a different measurement category");
            }
        }
    }

    /**
     * Centralized arithmetic helper.
     * Converts operands to base units and performs the operation.
     */
    private double performBaseArithmetic(
            Quantity<U> other,
            ArithmeticOperation operation) {

        double firstBase = unit.convertToBaseUnit(value);
        double secondBase =
                other.unit.convertToBaseUnit(other.value);

        if (!Double.isFinite(firstBase)
                || !Double.isFinite(secondBase)) {
            throw new IllegalArgumentException(
                    "Converted quantity values must be finite");
        }

        double result = operation.compute(firstBase, secondBase);

        if (!Double.isFinite(result)) {
            throw new ArithmeticException(
                    "Arithmetic result is not finite");
        }

        return result;
    }

    /**
     * Adds another quantity using the first operand's unit.
     */
    public Quantity<U> add(Quantity<U> other) {
        return add(other, unit);
    }

    /**
     * Adds another quantity in the specified target unit.
     */
    public Quantity<U> add(Quantity<U> other, U targetUnit) {

        validateArithmeticOperands(other, targetUnit, true);

        double resultInBase =
                performBaseArithmetic(other, ArithmeticOperation.ADD);

        double convertedResult =
                targetUnit.convertFromBaseUnit(resultInBase);

        return new Quantity<>(
                roundToTwoDecimals(convertedResult),
                targetUnit);
    }

    /**
     * Subtracts another quantity using the first operand's unit.
     */
    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, unit);
    }

    /**
     * Subtracts another quantity in the specified target unit.
     */
    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {

        validateArithmeticOperands(other, targetUnit, true);

        double resultInBase =
                performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);

        double convertedResult =
                targetUnit.convertFromBaseUnit(resultInBase);

        return new Quantity<>(
                roundToTwoDecimals(convertedResult),
                targetUnit);
    }

    /**
     * Divides two quantities of the same measurement category.
     * Returns a dimensionless double.
     */
    public double divide(Quantity<U> other) {

        validateArithmeticOperands(other, null, false);

        return performBaseArithmetic(
                other, ArithmeticOperation.DIVIDE);
    }

    /**
     * Validates the target unit for conversion.
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

    /**
     * Rounds arithmetic and conversion results to two decimals.
     */
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
