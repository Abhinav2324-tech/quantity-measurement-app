
package com.bridgelabz.quantitymeasurement;

/**
 * UC11: Volume units with conversion factors relative to litres.
 */
public enum VolumeUnit implements IMeasurable {

    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double conversionFactor;

    VolumeUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    /**
     * Converts the given volume to litres.
     */
    @Override
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    /**
     * Converts litres to the current unit.
     */
    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }

    @Override
    public String getUnitName() {
        return name();
    }
}
