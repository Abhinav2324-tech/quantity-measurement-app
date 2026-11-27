package com.bridgelabz.quantitymeasurement;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the standalone enum's conversion methods directly.
 */
class LengthUnitTest {

    @ParameterizedTest
    @CsvSource({
            "FEET, 1.0",
            "INCHES, 0.08333333333333333",
            "YARDS, 3.0",
            "CENTIMETERS, 0.03280839895013123"
    })
    void testConversionFactorIsRelativeToFeet(LengthUnit unit, double expected) {
        assertEquals(expected, unit.getConversionFactor(), 1e-9);
    }

    @ParameterizedTest
    @CsvSource({
            "FEET, 5.0, 5.0",
            "INCHES, 12.0, 1.0",
            "YARDS, 1.0, 3.0",
            "CENTIMETERS, 30.48, 1.0"
    })
    void testConvertToBaseUnit(LengthUnit unit, double value, double expectedFeet) {
        assertEquals(expectedFeet, unit.convertToBaseUnit(value), 1e-9);
    }

    @ParameterizedTest
    @CsvSource({
            "FEET, 2.0, 2.0",
            "INCHES, 1.0, 12.0",
            "YARDS, 3.0, 1.0",
            "CENTIMETERS, 1.0, 30.48"
    })
    void testConvertFromBaseUnit(LengthUnit unit, double feet, double expected) {
        assertEquals(expected, unit.convertFromBaseUnit(feet), 1e-9);
    }
}