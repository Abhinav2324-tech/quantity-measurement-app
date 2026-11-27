
package com.bridgelabz.quantitymeasurement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.bridgelabz.quantitymeasurement.TemperatureUnit.*;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTemperatureTest {

    // EQUALITY TESTS

    @Test
    void testSameUnitEquality() {
        assertEquals(
                new Quantity<>(25.0, CELSIUS),
                new Quantity<>(25.0, CELSIUS));
    }

    @Test
    void testCelsiusFahrenheitEquality() {
        assertEquals(
                new Quantity<>(0.0, CELSIUS),
                new Quantity<>(32.0, FAHRENHEIT));
    }

    @Test
    void testCelsiusKelvinEquality() {
        assertEquals(
                new Quantity<>(0.0, CELSIUS),
                new Quantity<>(273.15, KELVIN));
    }

    @Test
    void testBoilingPointEquality() {
        assertEquals(
                new Quantity<>(100.0, CELSIUS),
                new Quantity<>(212.0, FAHRENHEIT));

        assertEquals(
                new Quantity<>(100.0, CELSIUS),
                new Quantity<>(373.15, KELVIN));
    }

    @Test
    void testNegativeFortyEquality() {
        assertEquals(
                new Quantity<>(-40.0, CELSIUS),
                new Quantity<>(-40.0, FAHRENHEIT));
    }

    @Test
    void testDifferentTemperaturesNotEqual() {
        assertNotEquals(
                new Quantity<>(0.0, CELSIUS),
                new Quantity<>(100.0, CELSIUS));
    }

    // CONVERSION TESTS

    @ParameterizedTest
    @CsvSource({
            "0.0, CELSIUS, FAHRENHEIT, 32.0",
            "100.0, CELSIUS, FAHRENHEIT, 212.0",
            "-40.0, CELSIUS, FAHRENHEIT, -40.0",
            "32.0, FAHRENHEIT, CELSIUS, 0.0",
            "212.0, FAHRENHEIT, CELSIUS, 100.0",
            "0.0, CELSIUS, KELVIN, 273.15",
            "273.15, KELVIN, CELSIUS, 0.0",
            "373.15, KELVIN, CELSIUS, 100.0",
            "0.0, KELVIN, CELSIUS, -273.15",
            "100.0, CELSIUS, CELSIUS, 100.0"
    })
    void testTemperatureConversions(
            double input,
            TemperatureUnit source,
            TemperatureUnit target,
            double expected) {

        Quantity<TemperatureUnit> result =
                new Quantity<>(input, source).convertTo(target);

        assertEquals(expected, result.getValue(), 1e-9);
        assertEquals(target, result.getUnit());
    }

    @Test
    void testRoundTripConversion() {
        Quantity<TemperatureUnit> original =
                new Quantity<>(100.0, CELSIUS);

        Quantity<TemperatureUnit> result =
                original.convertTo(FAHRENHEIT).convertTo(CELSIUS);

        assertEquals(original, result);
    }

    // UNSUPPORTED ARITHMETIC

    @Test
    void testAdditionNotSupported() {
        Quantity<TemperatureUnit> first =
                new Quantity<>(100.0, CELSIUS);

        Quantity<TemperatureUnit> second =
                new Quantity<>(50.0, CELSIUS);

        assertThrows(UnsupportedOperationException.class,
                () -> first.add(second));
    }

    @Test
    void testSubtractionNotSupported() {
        Quantity<TemperatureUnit> first =
                new Quantity<>(100.0, CELSIUS);

        Quantity<TemperatureUnit> second =
                new Quantity<>(50.0, CELSIUS);

        assertThrows(UnsupportedOperationException.class,
                () -> first.subtract(second));
    }

    @Test
    void testDivisionNotSupported() {
        Quantity<TemperatureUnit> first =
                new Quantity<>(100.0, CELSIUS);

        Quantity<TemperatureUnit> second =
                new Quantity<>(50.0, CELSIUS);

        assertThrows(UnsupportedOperationException.class,
                () -> first.divide(second));
    }

    @Test
    void testUnsupportedOperationMessage() {
        Quantity<TemperatureUnit> first =
                new Quantity<>(100.0, CELSIUS);

        UnsupportedOperationException exception =
                assertThrows(
                        UnsupportedOperationException.class,
                        () -> first.add(
                                new Quantity<>(50.0, CELSIUS)));

        assertTrue(exception.getMessage().contains("Temperature"));
    }

    // OPERATION CAPABILITIES

    @Test
    void testTemperatureDoesNotSupportArithmetic() {
        assertFalse(CELSIUS.supportsArithmetic());
        assertFalse(FAHRENHEIT.supportsAddition());
        assertFalse(KELVIN.supportsSubtraction());
        assertFalse(KELVIN.supportsDivision());
    }

    @Test
    void testOtherCategoriesSupportArithmetic() {
        assertTrue(LengthUnit.FEET.supportsAddition());
        assertTrue(WeightUnit.KILOGRAM.supportsDivision());
        assertTrue(VolumeUnit.LITRE.supportsSubtraction());
    }

    // CROSS-CATEGORY TYPE SAFETY

    @Test
    void testTemperatureVsOtherCategories() {
        Quantity<TemperatureUnit> temperature =
                new Quantity<>(100.0, CELSIUS);

        assertNotEquals(
                temperature,
                new Quantity<>(100.0, LengthUnit.FEET));

        assertNotEquals(
                temperature,
                new Quantity<>(100.0, WeightUnit.KILOGRAM));

        assertNotEquals(
                temperature,
                new Quantity<>(100.0, VolumeUnit.LITRE));
    }

    // VALIDATION AND IMMUTABILITY

    @Test
    void testNullUnitRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<TemperatureUnit>(100.0, null));
    }

    @Test
    void testInvalidValueRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(Double.NaN, CELSIUS));
    }

    @Test
    void testNullConversionTargetRejected() {
        Quantity<TemperatureUnit> temperature =
                new Quantity<>(100.0, CELSIUS);

        assertThrows(IllegalArgumentException.class,
                () -> temperature.convertTo(null));
    }

    @Test
    void testConversionDoesNotModifyOriginal() {
        Quantity<TemperatureUnit> original =
                new Quantity<>(100.0, CELSIUS);

        Quantity<TemperatureUnit> converted =
                original.convertTo(FAHRENHEIT);

        assertNotSame(original, converted);
        assertEquals(100.0, original.getValue());
        assertEquals(CELSIUS, original.getUnit());
        assertEquals(212.0, converted.getValue());
    }

    @Test
    void testEquivalentTemperaturesHaveSameHashCode() {
        assertEquals(
                new Quantity<>(0.0, CELSIUS).hashCode(),
                new Quantity<>(32.0, FAHRENHEIT).hashCode());
    }

    @Test
    void testTemperatureUnitNames() {
        assertEquals("CELSIUS", CELSIUS.getUnitName());
        assertEquals("FAHRENHEIT", FAHRENHEIT.getUnitName());
        assertEquals("KELVIN", KELVIN.getUnitName());
    }
}
