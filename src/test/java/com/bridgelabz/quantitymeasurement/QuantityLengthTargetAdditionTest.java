package com.bridgelabz.quantitymeasurement;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.bridgelabz.quantitymeasurement.LengthUnit.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests addition with an explicitly selected result unit.
 */
class QuantityLengthTargetAdditionTest {

    @ParameterizedTest
    @CsvSource({
            "1.0, FEET, 12.0, INCHES, FEET, 2.0",
            "1.0, FEET, 12.0, INCHES, INCHES, 24.0",
            "1.0, FEET, 12.0, INCHES, YARDS, 0.6666666666666666",
            "1.0, INCHES, 1.0, INCHES, CENTIMETERS, 5.08",
            "2.0, YARDS, 3.0, FEET, YARDS, 3.0",
            "2.0, YARDS, 3.0, FEET, FEET, 9.0",
            "5.0, FEET, 0.0, INCHES, YARDS, 1.6666666666666667",
            "5.0, FEET, -2.0, FEET, INCHES, 36.0",
            "1000.0, FEET, 500.0, FEET, INCHES, 18000.0",
            "12.0, INCHES, 12.0, INCHES, YARDS, 0.6666666666666666"
    })
    void testAdditionInRequestedUnit(
            double firstValue, LengthUnit firstUnit,
            double secondValue, LengthUnit secondUnit,
            LengthUnit target, double expected) {

        QuantityLength first = new QuantityLength(firstValue, firstUnit);
        QuantityLength second = new QuantityLength(secondValue, secondUnit);

        QuantityLength result = QuantityLength.add(first, second, target);

        assertEquals(expected, result.getValue(), 1e-9);
        assertEquals(target, result.getUnit());
    }

    @Test
    void testChangingOrderWithSameTargetGivesSameResult() {
        QuantityLength foot = new QuantityLength(1.0, FEET);
        QuantityLength inches = new QuantityLength(12.0, INCHES);

        assertEquals(QuantityLength.add(foot, inches, YARDS),
                QuantityLength.add(inches, foot, YARDS));
    }

    @Test
    void testNullTargetIsRejected() {
        QuantityLength foot = new QuantityLength(1.0, FEET);
        QuantityLength inches = new QuantityLength(12.0, INCHES);

        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.add(foot, inches, null));
    }

    @Test
    void testNullOperandsAreRejectedWithAnExplicitTarget() {
        QuantityLength foot = new QuantityLength(1.0, FEET);

        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.add(null, foot, INCHES));
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.add(foot, null, INCHES));
    }

    @Test
    void testAllSourceAndTargetUnitCombinations() {
        Map<LengthUnit, Double> oneFoot = Map.of(
                FEET, 1.0,
                INCHES, 12.0,
                YARDS, 1.0 / 3.0,
                CENTIMETERS, 30.48);

        for (LengthUnit firstUnit : LengthUnit.values()) {
            for (LengthUnit secondUnit : LengthUnit.values()) {
                for (LengthUnit target : LengthUnit.values()) {
                    QuantityLength first =
                            new QuantityLength(oneFoot.get(firstUnit), firstUnit);
                    QuantityLength second =
                            new QuantityLength(oneFoot.get(secondUnit), secondUnit);

                    QuantityLength result = QuantityLength.add(first, second, target);

                    assertEquals(2 * oneFoot.get(target), result.getValue(), 1e-9);
                    assertEquals(target, result.getUnit());
                }
            }
        }
    }
}