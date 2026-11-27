
package com.bridgelabz.quantitymeasurement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class QuantityWeightTest {

    @Test
    void testSameUnitEquality() {
        assertEquals(
                new QuantityWeight(1.0, WeightUnit.KILOGRAM),
                new QuantityWeight(1.0, WeightUnit.KILOGRAM));
    }

    @Test
    void testDifferentValues() {
        assertNotEquals(
                new QuantityWeight(1.0, WeightUnit.KILOGRAM),
                new QuantityWeight(2.0, WeightUnit.KILOGRAM));
    }

    @Test
    void testKilogramGramEquality() {
        assertEquals(
                new QuantityWeight(1.0, WeightUnit.KILOGRAM),
                new QuantityWeight(1000.0, WeightUnit.GRAM));
    }

    @Test
    void testKilogramPoundEquality() {
        assertEquals(
                new QuantityWeight(1.0, WeightUnit.KILOGRAM),
                new QuantityWeight(2.20462, WeightUnit.POUND));
    }

    @Test
    void testGramPoundEquality() {
        assertEquals(
                new QuantityWeight(453.592, WeightUnit.GRAM),
                new QuantityWeight(1.0, WeightUnit.POUND));
    }

    @Test
    void testNullComparison() {
        assertNotEquals(
                new QuantityWeight(1.0, WeightUnit.KILOGRAM),
                null);
    }

    @Test
    void testDifferentClassComparison() {
        assertNotEquals(
                new QuantityWeight(1.0, WeightUnit.KILOGRAM),
                "1.0");
    }

    @Test
    void testSameReference() {
        QuantityWeight weight =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        assertEquals(weight, weight);
    }

    @ParameterizedTest
    @CsvSource({
            "1.0, KILOGRAM, GRAM, 1000.0",
            "1000.0, GRAM, KILOGRAM, 1.0",
            "1.0, POUND, KILOGRAM, 0.453592",
            "1.0, KILOGRAM, POUND, 2.2046244202",
            "500.0, GRAM, POUND, 1.1023122101"
    })
    void testWeightConversion(
            double value,
            WeightUnit source,
            WeightUnit target,
            double expected) {

        QuantityWeight result =
                new QuantityWeight(value, source)
                        .convertTo(target);

        assertEquals(expected, result.getValue(), 1e-6);
        assertEquals(target, result.getUnit());
    }

    @Test
    void testSameUnitAddition() {
        QuantityWeight result = QuantityWeight.add(
                new QuantityWeight(1.0, WeightUnit.KILOGRAM),
                new QuantityWeight(2.0, WeightUnit.KILOGRAM));

        assertEquals(3.0, result.getValue(), 1e-6);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    @Test
    void testCrossUnitAddition() {
        QuantityWeight result = QuantityWeight.add(
                new QuantityWeight(1.0, WeightUnit.KILOGRAM),
                new QuantityWeight(500.0, WeightUnit.GRAM));

        assertEquals(1.5, result.getValue(), 1e-6);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    @Test
    void testAdditionWithTargetUnit() {
        QuantityWeight result = QuantityWeight.add(
                new QuantityWeight(1.0, WeightUnit.KILOGRAM),
                new QuantityWeight(500.0, WeightUnit.GRAM),
                WeightUnit.GRAM);

        assertEquals(1500.0, result.getValue(), 1e-6);
        assertEquals(WeightUnit.GRAM, result.getUnit());
    }

    @Test
    void testAdditionIsCommutative() {
        QuantityWeight first =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        QuantityWeight second =
                new QuantityWeight(500.0, WeightUnit.GRAM);

        assertEquals(
                QuantityWeight.add(first, second, WeightUnit.GRAM),
                QuantityWeight.add(second, first, WeightUnit.GRAM));
    }

    @Test
    void testLengthWeightIncompatibility() {
        QuantityWeight weight =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        QuantityLength length =
                new QuantityLength(1.0, LengthUnit.FEET);

        assertNotEquals(weight, length);
    }

    @Test
    void testNullUnitRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityWeight(1.0, null));
    }

    @Test
    void testNaNRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityWeight(
                        Double.NaN, WeightUnit.KILOGRAM));
    }

    @Test
    void testInfinityRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityWeight(
                        Double.POSITIVE_INFINITY,
                        WeightUnit.KILOGRAM));
    }

    @Test
    void testNullTargetRejected() {
        QuantityWeight weight =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        assertThrows(IllegalArgumentException.class,
                () -> weight.convertTo(null));
    }

    @Test
    void testZeroAndNegativeAddition() {
        QuantityWeight result = QuantityWeight.add(
                new QuantityWeight(0.0, WeightUnit.KILOGRAM),
                new QuantityWeight(-500.0, WeightUnit.GRAM));

        assertEquals(-0.5, result.getValue(), 1e-6);
    }

    @Test
    void testOriginalObjectsRemainUnchanged() {
        QuantityWeight original =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        QuantityWeight converted =
                original.convertTo(WeightUnit.GRAM);

        assertNotSame(original, converted);
        assertEquals(1.0, original.getValue(), 0.0);
        assertEquals(WeightUnit.KILOGRAM, original.getUnit());
    }

    @Test
    void testEquivalentWeightsHaveSameHashCode() {
        assertEquals(
                new QuantityWeight(1.0, WeightUnit.KILOGRAM).hashCode(),
                new QuantityWeight(1000.0, WeightUnit.GRAM).hashCode());
    }
}
