package com.bridgelabz.quantitymeasurement;

import com.bridgelabz.quantitymeasurement.QuantityMeasurementApp.Feet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests value equality, reference equality, and invalid comparisons.
 */
class QuantityMeasurementAppTest {

    @Test
    void testFeetEquality_SameValue() {
        assertTrue(new Feet(1.0).equals(new Feet(1.0)));
    }

    @Test
    void testFeetEquality_DifferentValue() {
        assertFalse(new Feet(1.0).equals(new Feet(2.0)));
    }

    @Test
    void testFeetEquality_NullComparison() {
        assertFalse(new Feet(1.0).equals(null));
    }

    @Test
    void testFeetEquality_DifferentClass() {
        assertFalse(new Feet(1.0).equals("1.0"));
    }

    @Test
    void testFeetEquality_SameReference() {
        Feet measurement = new Feet(1.0);
        assertTrue(measurement.equals(measurement));
    }
}