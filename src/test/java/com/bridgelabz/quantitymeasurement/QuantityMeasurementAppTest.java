package com.bridgelabz.quantitymeasurement;

import com.bridgelabz.quantitymeasurement.QuantityMeasurementApp.Feet;
import com.bridgelabz.quantitymeasurement.QuantityMeasurementApp.Inches;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests equality for feet and inches separately.
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

    @Test
    void testInchesEquality_SameValue() {
        assertTrue(new Inches(1.0).equals(new Inches(1.0)));
    }

    @Test
    void testInchesEquality_DifferentValue() {
        assertFalse(new Inches(1.0).equals(new Inches(2.0)));
    }

    @Test
    void testInchesEquality_NullComparison() {
        assertFalse(new Inches(1.0).equals(null));
    }

    @Test
    void testInchesEquality_DifferentClass() {
        assertFalse(new Inches(1.0).equals("1.0"));
    }

    @Test
    void testInchesEquality_SameReference() {
        Inches measurement = new Inches(1.0);
        assertTrue(measurement.equals(measurement));
    }
}