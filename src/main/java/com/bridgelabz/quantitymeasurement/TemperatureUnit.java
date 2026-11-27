
package com.bridgelabz.quantitymeasurement;

import java.util.function.DoubleUnaryOperator;

/**
 * UC14: Temperature measurement units.
 * Base unit: Celsius.
 */
public enum TemperatureUnit implements IMeasurable {

    CELSIUS(
            celsius -> celsius,
            value -> value
    ),

    FAHRENHEIT(
            fahrenheit -> (fahrenheit - 32.0) * 5.0 / 9.0,
            celsius -> celsius * 9.0 / 5.0 + 32.0
    ),

    KELVIN(
            kelvin -> kelvin - 273.15,
            celsius -> celsius + 273.15
    );

    private final DoubleUnaryOperator toBase;
    private final DoubleUnaryOperator fromBase;

    private static final SupportsArithmetic ARITHMETIC_SUPPORT =
            () -> false;

    TemperatureUnit(
            DoubleUnaryOperator toBase,
            DoubleUnaryOperator fromBase) {

        this.toBase = toBase;
        this.fromBase = fromBase;
    }

    /**
     * Temperature conversions use formulas,
     * not a single multiplication factor.
     */
    @Override
    public double getConversionFactor() {
        return 1.0;
    }

    /**
     * Converts a temperature to Celsius.
     */
    @Override
    public double convertToBaseUnit(double value) {
        return toBase.applyAsDouble(value);
    }

    /**
     * Converts Celsius to the current unit.
     */
    @Override
    public double convertFromBaseUnit(double baseValue) {
        return fromBase.applyAsDouble(baseValue);
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public boolean supportsArithmetic() {
        return ARITHMETIC_SUPPORT.isSupported();
    }

    @Override
    public void validateOperationSupport(String operation) {
        throw new UnsupportedOperationException(
                "Temperature does not support "
                        + operation.toLowerCase()
                        + " on absolute temperatures");
    }
}
