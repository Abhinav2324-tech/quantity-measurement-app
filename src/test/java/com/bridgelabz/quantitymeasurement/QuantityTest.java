
package com.bridgelabz.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {

    @Test
    void testLengthEquality() {
        Quantity<LengthUnit> first =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> second =
                new Quantity<>(12.0, LengthUnit.INCHES);

        assertEquals(first, second);
    }

    @Test
    void testWeightEquality() {
        Quantity<WeightUnit> first =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> second =
                new Quantity<>(1000.0, WeightUnit.GRAM);

        assertEquals(first, second);
    }

    @Test
    void testPoundKilogramEquality() {
        assertEquals(
                new Quantity<>(1.0, WeightUnit.KILOGRAM),
                new Quantity<>(2.20462, WeightUnit.POUND));
    }

    @Test
    void testDifferentCategoriesNotEqual() {
        Quantity<LengthUnit> length =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<WeightUnit> weight =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        assertNotEquals(length, weight);
    }

    @Test
    void testLengthConversion() {
        Quantity<LengthUnit> length =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> result =
                length.convertTo(LengthUnit.INCHES);

        assertEquals(12.0, result.getValue());
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }

    @Test
    void testWeightConversion() {
        Quantity<WeightUnit> weight =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> result =
                weight.convertTo(WeightUnit.GRAM);

        assertEquals(1000.0, result.getValue());
        assertEquals(WeightUnit.GRAM, result.getUnit());
    }

    @Test
    void testConversionRounding() {
        Quantity<LengthUnit> length =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> result =
                length.convertTo(LengthUnit.YARDS);

        assertEquals(0.33, result.getValue());
    }

    @Test
    void testLengthAddition() {
        Quantity<LengthUnit> first =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> second =
                new Quantity<>(12.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result = first.add(second);

        assertEquals(2.0, result.getValue());
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testWeightAddition() {
        Quantity<WeightUnit> first =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> second =
                new Quantity<>(500.0, WeightUnit.GRAM);

        Quantity<WeightUnit> result =
                first.add(second, WeightUnit.GRAM);

        assertEquals(1500.0, result.getValue());
        assertEquals(WeightUnit.GRAM, result.getUnit());
    }

    @Test
    void testNullUnitRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(1.0, null));
    }

    @Test
    void testNaNRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(
                        Double.NaN, LengthUnit.FEET));
    }

    @Test
    void testNullTargetRejected() {
        Quantity<LengthUnit> length =
                new Quantity<>(1.0, LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class,
                () -> length.convertTo(null));
    }

    @Test
    void testOriginalObjectUnchanged() {
        Quantity<WeightUnit> original =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> converted =
                original.convertTo(WeightUnit.GRAM);

        assertNotSame(original, converted);
        assertEquals(1.0, original.getValue());
        assertEquals(WeightUnit.KILOGRAM, original.getUnit());
    }

    @Test
    void testEquivalentQuantitiesHaveSameHashCode() {
        assertEquals(
                new Quantity<>(1.0, LengthUnit.FEET).hashCode(),
                new Quantity<>(12.0, LengthUnit.INCHES).hashCode());
    }
}
