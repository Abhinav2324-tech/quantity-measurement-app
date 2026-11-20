package com.bridgelabz.quantitymeasurement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.bridgelabz.quantitymeasurement.LengthUnit.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies length addition, result units, and invalid inputs.
 */
class QuantityLengthAdditionTest {

    @ParameterizedTest
    @CsvSource({
            "1.0, FEET, 2.0, FEET, 3.0",
            "6.0, INCHES, 6.0, INCHES, 12.0",
            "1.0, FEET, 12.0, INCHES, 2.0",
            "12.0, INCHES, 1.0, FEET, 24.0",
            "1.0, YARDS, 3.0, FEET, 2.0",
            "36.0, INCHES, 1.0, YARDS, 72.0",
            "2.54, CENTIMETERS, 1.0, INCHES, 5.08",
            "5.0, FEET, 0.0, INCHES, 5.0",
            "5.0, FEET, -2.0, FEET, 3.0",
            "1000000.0, FEET, 1000000.0, FEET, 2000000.0",
            "0.001, FEET, 0.002, FEET, 0.003"
    })
    void testAdditionUsesFirstUnit(
            double firstValue, LengthUnit firstUnit,
            double secondValue, LengthUnit secondUnit,
            double expectedValue) {

        QuantityLength first = new QuantityLength(firstValue, firstUnit);
        QuantityLength second = new QuantityLength(secondValue, secondUnit);
        QuantityLength result = first.add(second);

        assertEquals(expectedValue, result.getValue(), 1e-9);
        assertEquals(firstUnit, result.getUnit());
    }

    @Test
    void testChangingOrderPreservesPhysicalLength() {
        QuantityLength foot = new QuantityLength(1.0, FEET);
        QuantityLength inches = new QuantityLength(12.0, INCHES);

        assertEquals(QuantityLength.add(foot, inches),
                QuantityLength.add(inches, foot));
    }

    @Test
    void testAdditionDoesNotChangeTheOriginalOperands() {
        QuantityLength first = new QuantityLength(1.0, FEET);
        QuantityLength second = new QuantityLength(12.0, INCHES);

        QuantityLength result = first.add(second);

        assertNotSame(first, result);
        assertEquals(1.0, first.getValue(), 0.0);
        assertEquals(FEET, first.getUnit());
        assertEquals(12.0, second.getValue(), 0.0);
        assertEquals(INCHES, second.getUnit());
    }

    @Test
    void testNullFirstOperandIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.add(null, new QuantityLength(1.0, FEET)));
    }

    @Test
    void testNullSecondOperandIsRejected() {
        QuantityLength first = new QuantityLength(1.0, FEET);

        assertThrows(IllegalArgumentException.class, () -> first.add(null));
    }

    @Test
    void testAdditionOverflowIsRejected() {
        QuantityLength large = new QuantityLength(Double.MAX_VALUE, FEET);

        assertThrows(IllegalArgumentException.class, () -> large.add(large));
    }
}