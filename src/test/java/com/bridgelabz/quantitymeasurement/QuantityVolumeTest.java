
package com.bridgelabz.quantitymeasurement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.bridgelabz.quantitymeasurement.VolumeUnit.*;
import static org.junit.jupiter.api.Assertions.*;

class QuantityVolumeTest {

    @Test
    void testSameUnitEquality() {
        assertEquals(
                new Quantity<>(1.0, LITRE),
                new Quantity<>(1.0, LITRE));
    }

    @Test
    void testDifferentValues() {
        assertNotEquals(
                new Quantity<>(1.0, LITRE),
                new Quantity<>(2.0, LITRE));
    }

    @Test
    void testLitreMillilitreEquality() {
        assertEquals(
                new Quantity<>(1.0, LITRE),
                new Quantity<>(1000.0, MILLILITRE));
    }

    @Test
    void testLitreGallonEquality() {
        assertEquals(
                new Quantity<>(3.78541, LITRE),
                new Quantity<>(1.0, GALLON));
    }

    @Test
    void testApproximateGallonEquality() {
        assertEquals(
                new Quantity<>(1.0, LITRE),
                new Quantity<>(0.264172, GALLON));
    }

    @Test
    void testSymmetricEquality() {
        Quantity<VolumeUnit> first =
                new Quantity<>(1.0, LITRE);

        Quantity<VolumeUnit> second =
                new Quantity<>(1000.0, MILLILITRE);

        assertEquals(first, second);
        assertEquals(second, first);
    }

    @Test
    void testDifferentCategoriesNotEqual() {
        Quantity<VolumeUnit> volume =
                new Quantity<>(1.0, LITRE);

        assertNotEquals(volume,
                new Quantity<>(1.0, LengthUnit.FEET));

        assertNotEquals(volume,
                new Quantity<>(1.0, WeightUnit.KILOGRAM));
    }

    @ParameterizedTest
    @CsvSource({
            "1.0, LITRE, MILLILITRE, 1000.0",
            "1000.0, MILLILITRE, LITRE, 1.0",
            "1.0, GALLON, LITRE, 3.79",
            "3.78541, LITRE, GALLON, 1.0",
            "0.0, LITRE, MILLILITRE, 0.0",
            "-1.0, LITRE, MILLILITRE, -1000.0"
    })
    void testVolumeConversion(
            double value,
            VolumeUnit source,
            VolumeUnit target,
            double expected) {

        Quantity<VolumeUnit> result =
                new Quantity<>(value, source).convertTo(target);

        assertEquals(expected, result.getValue(), 1e-9);
        assertEquals(target, result.getUnit());
    }

    @Test
    void testSameUnitAddition() {
        Quantity<VolumeUnit> result =
                new Quantity<>(1.0, LITRE)
                        .add(new Quantity<>(2.0, LITRE));

        assertEquals(3.0, result.getValue(), 1e-9);
        assertEquals(LITRE, result.getUnit());
    }

    @Test
    void testCrossUnitAddition() {
        Quantity<VolumeUnit> result =
                new Quantity<>(1.0, LITRE)
                        .add(new Quantity<>(1000.0, MILLILITRE));

        assertEquals(2.0, result.getValue(), 1e-9);
        assertEquals(LITRE, result.getUnit());
    }

    @Test
    void testAdditionWithExplicitTargetUnit() {
        Quantity<VolumeUnit> result =
                new Quantity<>(1.0, LITRE)
                        .add(new Quantity<>(1000.0, MILLILITRE),
                                MILLILITRE);

        assertEquals(2000.0, result.getValue(), 1e-9);
        assertEquals(MILLILITRE, result.getUnit());
    }

    @Test
    void testGallonLitreAddition() {
        Quantity<VolumeUnit> result =
                new Quantity<>(1.0, GALLON)
                        .add(new Quantity<>(3.78541, LITRE));

        assertEquals(2.0, result.getValue(), 1e-9);
        assertEquals(GALLON, result.getUnit());
    }

    @Test
    void testNegativeAddition() {
        Quantity<VolumeUnit> result =
                new Quantity<>(5.0, LITRE)
                        .add(new Quantity<>(-2000.0, MILLILITRE));

        assertEquals(3.0, result.getValue(), 1e-9);
    }

    @Test
    void testNullUnitRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<VolumeUnit>(1.0, null));
    }

    @Test
    void testNullTargetRejected() {
        Quantity<VolumeUnit> volume =
                new Quantity<>(1.0, LITRE);

        assertThrows(IllegalArgumentException.class,
                () -> volume.convertTo(null));
    }

    @Test
    void testNaNRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(
                        Double.NaN, LITRE));
    }

    @Test
    void testOriginalObjectUnchanged() {
        Quantity<VolumeUnit> original =
                new Quantity<>(1.0, LITRE);

        Quantity<VolumeUnit> converted =
                original.convertTo(MILLILITRE);

        assertNotSame(original, converted);
        assertEquals(1.0, original.getValue(), 0.0);
        assertEquals(LITRE, original.getUnit());
    }

    @Test
    void testEquivalentVolumesHaveSameHashCode() {
        assertEquals(
                new Quantity<>(1.0, LITRE).hashCode(),
                new Quantity<>(1000.0, MILLILITRE).hashCode());
    }
}
