
package com.bridgelabz.quantitymeasurement.model;

import com.bridgelabz.quantitymeasurement.IMeasurable;
import com.bridgelabz.quantitymeasurement.Quantity;

/**
 * UC15: Generic internal model for a measurement.
 */
public final class QuantityModel<U extends IMeasurable> {

    private final double value;
    private final U unit;

    public QuantityModel(double value, U unit) {
        // Reuse existing domain validation.
        new Quantity<>(value, unit);

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    public Quantity<U> toQuantity() {
        return new Quantity<>(value, unit);
    }
}

