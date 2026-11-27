
package com.bridgelabz.quantitymeasurement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class VolumeUnitTest {

    @ParameterizedTest
    @CsvSource({
            "LITRE, 1.0",
            "MILLILITRE, 0.001",
            "GALLON, 3.78541"
    })
    void testConversionFactors(
            VolumeUnit unit, double expected) {

        assertEquals(expected, unit.getConversionFactor(), 1e-9);
    }

    @Test
    void testConvertMillilitresToBaseUnit() {
        assertEquals(1.0,
                VolumeUnit.MILLILITRE.convertToBaseUnit(1000.0),
                1e-9);
    }

    @Test
    void testConvertGallonToBaseUnit() {
        assertEquals(3.78541,
                VolumeUnit.GALLON.convertToBaseUnit(1.0),
                1e-9);
    }

    @Test
    void testConvertLitresToMillilitres() {
        assertEquals(1000.0,
                VolumeUnit.MILLILITRE.convertFromBaseUnit(1.0),
                1e-9);
    }

    @Test
    void testConvertLitresToGallons() {
        assertEquals(1.0 / 3.78541,
                VolumeUnit.GALLON.convertFromBaseUnit(1.0),
                1e-9);
    }

    @Test
    void testVolumeUnitNames() {
        assertEquals("LITRE", VolumeUnit.LITRE.getUnitName());
        assertEquals("MILLILITRE",
                VolumeUnit.MILLILITRE.getUnitName());
        assertEquals("GALLON", VolumeUnit.GALLON.getUnitName());
    }
}
