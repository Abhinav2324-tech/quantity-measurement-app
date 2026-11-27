
package com.bridgelabz.quantitymeasurement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.bridgelabz.quantitymeasurement.LengthUnit.*;
import static org.junit.jupiter.api.Assertions.*;

class QuantitySubtractionDivisionTest {

    // SUBTRACTION TESTS

    @Test
    void testSameUnitSubtraction() {
        Quantity<LengthUnit> result =
                new Quantity<>(10.0, FEET)
                        .subtract(new Quantity<>(5.0, FEET));

        assertEquals(5.0, result.getValue());
        assertEquals(FEET, result.getUnit());
    }

    @Test
    void testCrossUnitSubtraction() {
        Quantity<LengthUnit> result =
                new Quantity<>(10.0, FEET)
                        .subtract(new Quantity<>(6.0, INCHES));

        assertEquals(9.5, result.getValue());
        assertEquals(FEET, result.getUnit());
    }

    @Test
    void testSubtractionWithTargetUnit() {
        Quantity<LengthUnit> result =
                new Quantity<>(10.0, FEET)
                        .subtract(
                                new Quantity<>(6.0, INCHES),
                                INCHES);

        assertEquals(114.0, result.getValue());
        assertEquals(INCHES, result.getUnit());
    }

    @Test
    void testWeightSubtraction() {
        Quantity<WeightUnit> result =
                new Quantity<>(10.0, WeightUnit.KILOGRAM)
                        .subtract(
                                new Quantity<>(5000.0, WeightUnit.GRAM));

        assertEquals(5.0, result.getValue());
    }

    @Test
    void testVolumeSubtraction() {
        Quantity<VolumeUnit> result =
                new Quantity<>(5.0, VolumeUnit.LITRE)
                        .subtract(
                                new Quantity<>(500.0, VolumeUnit.MILLILITRE));

        assertEquals(4.5, result.getValue());
    }

    @Test
    void testNegativeSubtraction() {
        Quantity<LengthUnit> result =
                new Quantity<>(5.0, FEET)
                        .subtract(new Quantity<>(10.0, FEET));

        assertEquals(-5.0, result.getValue());
    }

    @Test
    void testSubtractionResultingInZero() {
        Quantity<LengthUnit> result =
                new Quantity<>(10.0, FEET)
                        .subtract(new Quantity<>(120.0, INCHES));

        assertEquals(0.0, result.getValue());
    }

    @Test
    void testSubtractionWithNegativeOperand() {
        Quantity<LengthUnit> result =
                new Quantity<>(5.0, FEET)
                        .subtract(new Quantity<>(-2.0, FEET));

        assertEquals(7.0, result.getValue());
    }

    @Test
    void testSubtractionNonCommutative() {
        Quantity<LengthUnit> first =
                new Quantity<>(10.0, FEET);

        Quantity<LengthUnit> second =
                new Quantity<>(5.0, FEET);

        assertNotEquals(
                first.subtract(second),
                second.subtract(first));
    }

    @Test
    void testNullSubtractionOperand() {
        Quantity<LengthUnit> quantity =
                new Quantity<>(10.0, FEET);

        assertThrows(IllegalArgumentException.class,
                () -> quantity.subtract(null));
    }

    @Test
    void testNullSubtractionTarget() {
        Quantity<LengthUnit> quantity =
                new Quantity<>(10.0, FEET);

        assertThrows(IllegalArgumentException.class,
                () -> quantity.subtract(
                        new Quantity<>(5.0, FEET), null));
    }

    @Test
    void testChainedSubtraction() {
        Quantity<LengthUnit> result =
                new Quantity<>(10.0, FEET)
                        .subtract(new Quantity<>(2.0, FEET))
                        .subtract(new Quantity<>(1.0, FEET));

        assertEquals(7.0, result.getValue());
    }

    // DIVISION TESTS

    @ParameterizedTest
    @CsvSource({
            "10.0, FEET, 2.0, FEET, 5.0",
            "10.0, FEET, 5.0, FEET, 2.0",
            "24.0, INCHES, 2.0, FEET, 1.0",
            "12.0, INCHES, 1.0, FEET, 1.0",
            "5.0, FEET, 10.0, FEET, 0.5",
            "10.0, FEET, 10.0, FEET, 1.0",
            "0.0, FEET, 10.0, FEET, 0.0",
            "-10.0, FEET, 2.0, FEET, -5.0"
    })
    void testLengthDivision(
            double firstValue,
            LengthUnit firstUnit,
            double secondValue,
            LengthUnit secondUnit,
            double expected) {

        Quantity<LengthUnit> first =
                new Quantity<>(firstValue, firstUnit);

        Quantity<LengthUnit> second =
                new Quantity<>(secondValue, secondUnit);

        assertEquals(expected, first.divide(second), 1e-9);
    }

    @Test
    void testWeightDivision() {
        Quantity<WeightUnit> first =
                new Quantity<>(2000.0, WeightUnit.GRAM);

        Quantity<WeightUnit> second =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        assertEquals(2.0, first.divide(second), 1e-9);
    }

    @Test
    void testVolumeDivision() {
        Quantity<VolumeUnit> first =
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        Quantity<VolumeUnit> second =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        assertEquals(1.0, first.divide(second), 1e-9);
    }

    @Test
    void testDivisionByZero() {
        Quantity<LengthUnit> first =
                new Quantity<>(10.0, FEET);

        Quantity<LengthUnit> zero =
                new Quantity<>(0.0, FEET);

        assertThrows(ArithmeticException.class,
                () -> first.divide(zero));
    }

    @Test
    void testNullDivisionOperand() {
        Quantity<LengthUnit> first =
                new Quantity<>(10.0, FEET);

        assertThrows(IllegalArgumentException.class,
                () -> first.divide(null));
    }

    @Test
    void testDivisionNonCommutative() {
        Quantity<LengthUnit> first =
                new Quantity<>(10.0, FEET);

        Quantity<LengthUnit> second =
                new Quantity<>(5.0, FEET);

        assertNotEquals(
                first.divide(second),
                second.divide(first));
    }

    @Test
    void testDivisionLargeRatio() {
        Quantity<WeightUnit> first =
                new Quantity<>(1000000.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> second =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        assertEquals(1000000.0, first.divide(second), 1e-9);
    }

    @Test
    void testDivisionSmallRatio() {
        Quantity<WeightUnit> first =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> second =
                new Quantity<>(1000000.0, WeightUnit.KILOGRAM);

        assertEquals(0.000001, first.divide(second), 1e-12);
    }

    // INTEGRATION AND IMMUTABILITY TESTS

    @Test
    void testSubtractionAdditionInverse() {
        Quantity<LengthUnit> first =
                new Quantity<>(10.0, FEET);

        Quantity<LengthUnit> second =
                new Quantity<>(2.0, FEET);

        assertEquals(first, first.add(second).subtract(second));
    }

    @Test
    void testSubtractionImmutability() {
        Quantity<LengthUnit> original =
                new Quantity<>(10.0, FEET);

        Quantity<LengthUnit> second =
                new Quantity<>(5.0, FEET);

        Quantity<LengthUnit> result = original.subtract(second);

        assertNotSame(original, result);
        assertEquals(10.0, original.getValue());
        assertEquals(5.0, second.getValue());
    }

    @Test
    void testDivisionImmutability() {
        Quantity<LengthUnit> original =
                new Quantity<>(10.0, FEET);

        Quantity<LengthUnit> second =
                new Quantity<>(2.0, FEET);

        double result = original.divide(second);

        assertEquals(5.0, result);
        assertEquals(10.0, original.getValue());
        assertEquals(2.0, second.getValue());
    }

    @Test
    void testSubtractThenDivide() {
        Quantity<LengthUnit> result =
                new Quantity<>(10.0, FEET)
                        .subtract(new Quantity<>(2.0, FEET));

        assertEquals(4.0,
                result.divide(new Quantity<>(2.0, FEET)));
    }
}
