package com.bridgelabz.quantitymeasurement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies same-unit behavior and cross-unit equality after the refactor.
 */
class QuantityMeasurementAppTest {

    @ParameterizedTest
    @EnumSource(LengthUnit.class)
    void testSameUnit_SameValue(LengthUnit unit) {
        assertTrue(new QuantityLength(1.0, unit).equals(new QuantityLength(1.0, unit)));
    }

    @ParameterizedTest
    @EnumSource(LengthUnit.class)
    void testSameUnit_DifferentValue(LengthUnit unit) {
        assertFalse(new QuantityLength(1.0, unit).equals(new QuantityLength(2.0, unit)));
    }

    @ParameterizedTest
    @EnumSource(LengthUnit.class)
    void testNullComparison(LengthUnit unit) {
        assertFalse(new QuantityLength(1.0, unit).equals(null));
    }

    @ParameterizedTest
    @EnumSource(LengthUnit.class)
    void testDifferentClass(LengthUnit unit) {
        assertFalse(new QuantityLength(1.0, unit).equals("1.0"));
    }

    @ParameterizedTest
    @EnumSource(LengthUnit.class)
    void testSameReference(LengthUnit unit) {
        QuantityLength length = new QuantityLength(1.0, unit);
        assertTrue(length.equals(length));
    }

    @Test
    void testFeetToInches_EquivalentValue() {
        assertEquals(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCHES));
    }

    @Test
    void testInchesToFeet_EquivalentValue() {
        assertEquals(new QuantityLength(12.0, LengthUnit.INCHES),
                new QuantityLength(1.0, LengthUnit.FEET));
    }

    @Test
    void testCrossUnit_DifferentValue() {
        assertNotEquals(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(10.0, LengthUnit.INCHES));
    }

    @Test
    void testNullUnit_IsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityLength(1.0, null));
    }

    @Test
    void testNaN_IsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityLength(Double.NaN, LengthUnit.FEET));
    }

    @Test
    void testEquivalentLengths_HaveSameHashCode() {
        assertEquals(new QuantityLength(1.0, LengthUnit.FEET).hashCode(),
                new QuantityLength(12.0, LengthUnit.INCHES).hashCode());
    }
}