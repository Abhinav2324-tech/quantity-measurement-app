package com.bridgelabz.quantitymeasurement;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Owns length unit conversions to and from feet.
 */
public enum LengthUnit implements IMeasurable {
    FEET(12.0),
    INCHES(1.0),
    YARDS(36.0),
    CENTIMETERS(1.0 / 2.54);

    private static final BigDecimal INCHES_PER_FOOT = BigDecimal.valueOf(12);
    private final BigDecimal inchesPerUnit;

    LengthUnit(double inchesPerUnit) {
        this.inchesPerUnit = BigDecimal.valueOf(inchesPerUnit);
    }

    public double getConversionFactor() {
        return convertToBaseUnit(1.0);
    }

    public double convertToBaseUnit(double value) {
        return convertToBaseUnit(BigDecimal.valueOf(value)).doubleValue();
    }

    public double convertFromBaseUnit(double baseValue) {
        return convertFromBaseUnit(BigDecimal.valueOf(baseValue)).doubleValue();
    }

    @Override
    public String getUnitName() {
        return "";
    }

    BigDecimal convertToBaseUnit(BigDecimal value) {
        return toInches(value).divide(INCHES_PER_FOOT, MathContext.DECIMAL128);
    }

    BigDecimal convertFromBaseUnit(BigDecimal baseValue) {
        return baseValue.multiply(INCHES_PER_FOOT)
                .divide(inchesPerUnit, MathContext.DECIMAL128);
    }

    BigDecimal toInches(BigDecimal value) {
        return value.multiply(inchesPerUnit);
    }
}