
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
}
