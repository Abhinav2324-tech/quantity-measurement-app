package com.bridgelabz.quantitymeasurement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.bridgelabz.quantitymeasurement.LengthUnit.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests conversion values, round trips, immutability, and invalid inputs.
 */
class QuantityLengthConversionTest {

    @ParameterizedTest
    @CsvSource({
            "1.0, FEET, INCHES, 12.0",
            "24.0, INCHES, FEET, 2.0",
            "1.0, YARDS, INCHES, 36.0",
            "72.0, INCHES, YARDS, 2.0",
            "2.54, CENTIMETERS, INCHES, 1.0",
            "6.0, FEET, YARDS, 2.0",
            "3.0, YARDS, FEET, 9.0",
            "1.0, CENTIMETERS, INCHES, 0.3937007874",
            "5.0, FEET, FEET, 5.0",
            "0.0, FEET, INCHES, 0.0",
            "-1.0, FEET, INCHES, -12.0"
    })
    void testConvert(double value, LengthUnit source, LengthUnit target, double expected) {
        assertEquals(expected, QuantityLength.convert(value, source, target), 1e-9);
    }

    @Test
    void testRoundTripPreservesValue() {
        double centimeters = QuantityLength.convert(2.75, YARDS, CENTIMETERS);
        assertEquals(2.75, QuantityLength.convert(centimeters, CENTIMETERS, YARDS), 1e-9);
    }

    @Test
    void testConvertToReturnsNewObjectWithoutChangingOriginal() {
        QuantityLength original = new QuantityLength(1.0, FEET);
        QuantityLength converted = original.convertTo(INCHES);

        assertNotSame(original, converted);
        assertEquals(FEET, original.getUnit());
        assertEquals(1.0, original.getValue(), 0.0);
        assertEquals(INCHES, converted.getUnit());
        assertEquals(12.0, converted.getValue(), 0.0);
    }

    @Test
    void testNullSourceIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(1.0, null, INCHES));
    }

    @Test
    void testNullTargetIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(1.0, FEET, null));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void testNonFiniteValuesAreRejected(double value) {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(value, FEET, INCHES));
    }

    @Test
    void testLargeValueWithRepresentableResult() {
        assertEquals(3e307, QuantityLength.convert(1e307, YARDS, FEET), 3e295);
    }

    @Test
    void testSmallValueIsNotRoundedToZero() {
        assertEquals(1.2e-8, QuantityLength.convert(1e-9, FEET, INCHES), 1e-18);
    }

    @Test
    void testOverflowIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(Double.MAX_VALUE, YARDS, INCHES));
    }

    @Test
    void testUnderflowIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(Double.MIN_VALUE, INCHES, YARDS));
    }
}