package com.bridgelabz.quantitymeasurement;

/**
 * Defines how many inches are in one unit of each supported length.
 */
public enum LengthUnit {
    FEET(12.0),
    INCHES(1.0),
    YARDS(36.0),
    CENTIMETERS(1.0 / 2.54);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }
}