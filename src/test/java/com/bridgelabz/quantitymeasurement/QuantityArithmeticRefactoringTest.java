
package com.bridgelabz.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static com.bridgelabz.quantitymeasurement.LengthUnit.*;
import static org.junit.jupiter.api.Assertions.*;

class QuantityArithmeticRefactoringTest {

    @Test
    void testAdditionBehaviorPreserved() {
        Quantity<LengthUnit> first = new Quantity<>(1.0, FEET);
        Quantity<LengthUnit> second = new Quantity<>(12.0, INCHES);

        Quantity<LengthUnit> result = first.add(second);

        assertEquals(2.0, result.getValue());
        assertEquals(FEET, result.getUnit());
    }

    @Test
    void testAdditionWithExplicitTarget() {
        Quantity<LengthUnit> first = new Quantity<>(1.0, FEET);
        Quantity<LengthUnit> second = new Quantity<>(12.0, INCHES);

        Quantity<LengthUnit> result = first.add(second, INCHES);

        assertEquals(24.0, result.getValue());
        assertEquals(INCHES, result.getUnit());
    }

    @Test
    void testSubtractionBehaviorPreserved() {
        Quantity<LengthUnit> first = new Quantity<>(10.0, FEET);
        Quantity<LengthUnit> second = new Quantity<>(6.0, INCHES);

        Quantity<LengthUnit> result = first.subtract(second);

        assertEquals(9.5, result.getValue());
        assertEquals(FEET, result.getUnit());
    }

    @Test
    void testSubtractionWithExplicitTarget() {
        Quantity<LengthUnit> first = new Quantity<>(10.0, FEET);
        Quantity<LengthUnit> second = new Quantity<>(6.0, INCHES);

        Quantity<LengthUnit> result = first.subtract(second, INCHES);

        assertEquals(114.0, result.getValue());
        assertEquals(INCHES, result.getUnit());
    }

    @Test
    void testDivisionBehaviorPreserved() {
        Quantity<LengthUnit> first = new Quantity<>(24.0, INCHES);
        Quantity<LengthUnit> second = new Quantity<>(2.0, FEET);

        assertEquals(1.0, first.divide(second), 1e-9);
    }

    @Test
    void testNullValidationConsistency() {
        Quantity<LengthUnit> quantity = new Quantity<>(10.0, FEET);

        IllegalArgumentException addException =
                assertThrows(IllegalArgumentException.class,
                        () -> quantity.add(null));

        IllegalArgumentException subtractException =
                assertThrows(IllegalArgumentException.class,
                        () -> quantity.subtract(null));

        IllegalArgumentException divideException =
                assertThrows(IllegalArgumentException.class,
                        () -> quantity.divide(null));

        assertEquals(addException.getMessage(),
                subtractException.getMessage());

        assertEquals(subtractException.getMessage(),
                divideException.getMessage());
    }

    @Test
    void testNullTargetValidation() {
        Quantity<LengthUnit> first = new Quantity<>(10.0, FEET);
        Quantity<LengthUnit> second = new Quantity<>(5.0, FEET);

        assertThrows(IllegalArgumentException.class,
                () -> first.add(second, null));

        assertThrows(IllegalArgumentException.class,
                () -> first.subtract(second, null));
    }

    @Test
    void testDivisionByZero() {
        Quantity<LengthUnit> first = new Quantity<>(10.0, FEET);
        Quantity<LengthUnit> zero = new Quantity<>(0.0, FEET);

        assertThrows(ArithmeticException.class,
                () -> first.divide(zero));
    }

    @Test
    void testWeightArithmetic() {
        Quantity<WeightUnit> first =
                new Quantity<>(10.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> second =
                new Quantity<>(5000.0, WeightUnit.GRAM);

        assertEquals(15.0, first.add(second).getValue());
        assertEquals(5.0, first.subtract(second).getValue());
        assertEquals(2.0, first.divide(second), 1e-9);
    }

    @Test
    void testVolumeArithmetic() {
        Quantity<VolumeUnit> first =
                new Quantity<>(5.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> second =
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        assertEquals(6.0, first.add(second).getValue());
        assertEquals(4.0, first.subtract(second).getValue());
        assertEquals(5.0, first.divide(second), 1e-9);
    }

    @Test
    void testAdditionAndSubtractionRounding() {
        Quantity<LengthUnit> first = new Quantity<>(1.0, FEET);
        Quantity<LengthUnit> second = new Quantity<>(1.0, INCHES);

        assertEquals(1.08, first.add(second).getValue());
        assertEquals(0.92, first.subtract(second).getValue());
    }

    @Test
    void testDivisionDoesNotRoundToTwoDecimals() {
        Quantity<LengthUnit> first = new Quantity<>(1.0, FEET);
        Quantity<LengthUnit> second = new Quantity<>(3.0, FEET);

        assertEquals(1.0 / 3.0, first.divide(second), 1e-12);
    }

    @Test
    void testOriginalObjectsRemainUnchanged() {
        Quantity<LengthUnit> first = new Quantity<>(10.0, FEET);
        Quantity<LengthUnit> second = new Quantity<>(2.0, FEET);

        first.add(second);
        first.subtract(second);
        first.divide(second);

        assertEquals(10.0, first.getValue());
        assertEquals(2.0, second.getValue());
    }
}
