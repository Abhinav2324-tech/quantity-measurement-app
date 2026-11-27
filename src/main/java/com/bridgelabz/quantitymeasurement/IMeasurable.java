
package com.bridgelabz.quantitymeasurement;

/**
 * UC14: Common interface for measurement units.
 * Provides unit conversions and optional arithmetic support.
 */
public interface IMeasurable {

    double getConversionFactor();

    double convertToBaseUnit(double value);

    double convertFromBaseUnit(double baseValue);

    String getUnitName();

    // Existing categories support arithmetic by default.
    SupportsArithmetic DEFAULT_ARITHMETIC_SUPPORT = () -> true;

    default boolean supportsArithmetic() {
        return DEFAULT_ARITHMETIC_SUPPORT.isSupported();
    }

    default boolean supportsAddition() {
        return supportsArithmetic();
    }

    default boolean supportsSubtraction() {
        return supportsArithmetic();
    }

    default boolean supportsDivision() {
        return supportsArithmetic();
    }

    /**
     * Validates whether a particular operation is supported.
     */
    default void validateOperationSupport(String operation) {

        boolean supported = switch (operation.toUpperCase()) {
            case "ADD", "ADDITION" -> supportsAddition();
            case "SUBTRACT", "SUBTRACTION" -> supportsSubtraction();
            case "DIVIDE", "DIVISION" -> supportsDivision();
            default -> throw new IllegalArgumentException(
                    "Unknown arithmetic operation: " + operation);
        };

        if (!supported) {
            throw new UnsupportedOperationException(
                    getUnitName() + " does not support " + operation);
        }
    }

    /**
     * UC15: Identifies the measurement category.
     */
    default String getMeasurementType() {
        return getClass()
                .getSimpleName()
                .replace("Unit", "")
                .toUpperCase();
    }

    /**
     * UC15: Resolves another unit in the same measurement category.
     */

    default IMeasurable getUnitInstance(String unitName) {

        if (unitName == null || unitName.isBlank()) {
            throw new IllegalArgumentException(
                    "Unit name cannot be null or empty");
        }

        if (!(this instanceof Enum<?> currentUnit)) {
            throw new IllegalArgumentException(
                    "Measurement unit must be an enum");
        }

        Class<?> enumClass = currentUnit.getDeclaringClass();

        Object[] availableUnits = enumClass.getEnumConstants();

        for (Object candidate : availableUnits) {

            IMeasurable measurable = (IMeasurable) candidate;

            if (measurable.getUnitName()
                    .equalsIgnoreCase(unitName.trim())) {
                return measurable;
            }
        }

        throw new IllegalArgumentException(
                "Unknown unit for "
                        + getMeasurementType()
                        + ": " + unitName);
    }


}
