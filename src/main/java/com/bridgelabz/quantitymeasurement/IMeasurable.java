
package com.bridgelabz.quantitymeasurement;

/**
 * UC10: Common interface for all measurement units.
 *
 * Each measurement category provides its own conversion logic.
 */
public interface IMeasurable {

    double getConversionFactor();

    double convertToBaseUnit(double value);

    double convertFromBaseUnit(double baseValue);

    String getUnitName();
}
