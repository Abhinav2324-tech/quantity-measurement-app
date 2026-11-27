package com.bridgelabz.quantitymeasurement;

/**
 * UC 3 — Demonstrates same-unit and cross-unit length equality.
 */
public class QuantityMeasurementApp {

    public static boolean demonstrateLengthEquality(
            QuantityLength first, QuantityLength second) {
        return first.equals(second);
    }

    public static void demonstrateFeetEquality() {
        boolean equal = demonstrateLengthEquality(
                new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(1.0, LengthUnit.FEET));

        System.out.println("Feet to feet equal: " + equal);
    }

    public static void demonstrateInchesEquality() {
        boolean equal = demonstrateLengthEquality(
                new QuantityLength(1.0, LengthUnit.INCHES),
                new QuantityLength(1.0, LengthUnit.INCHES));

        System.out.println("Inches to inches equal: " + equal);
    }

    public static void demonstrateFeetInchesComparison() {
        boolean equal = demonstrateLengthEquality(
                new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCHES));

        System.out.println("1 foot equals 12 inches: " + equal);
    }
    public static QuantityLength demonstrateLengthConversion(
            double value, LengthUnit source, LengthUnit target) {
        return new QuantityLength(value, source).convertTo(target);
    }

    public static QuantityLength demonstrateLengthConversion(
            QuantityLength length, LengthUnit target) {
        return length.convertTo(target);
    }
    public static QuantityLength demonstrateLengthAddition(
            QuantityLength first, QuantityLength second) {
        return QuantityLength.add(first, second);
    }
    public static QuantityLength demonstrateLengthAddition(
            QuantityLength first, QuantityLength second, LengthUnit targetUnit) {
        return QuantityLength.add(first, second, targetUnit);
    }


    /**
     * Demonstrates equality between two weights.
     */
    public static boolean demonstrateWeightEquality(
            QuantityWeight first,
            QuantityWeight second) {

        return first.equals(second);
    }

    /**
     * Demonstrates weight conversion.
     */
    public static QuantityWeight demonstrateWeightConversion(
            QuantityWeight weight,
            WeightUnit targetUnit) {

        return weight.convertTo(targetUnit);
    }

    /**
     * Demonstrates weight addition.
     */
    public static QuantityWeight demonstrateWeightAddition(
            QuantityWeight first,
            QuantityWeight second,
            WeightUnit targetUnit) {

        return QuantityWeight.add(first, second, targetUnit);
    }

    public static void main(String[] args) {
        demonstrateFeetEquality();
        demonstrateInchesEquality();
        demonstrateFeetInchesComparison();

        System.out.println("1 yard equals 3 feet: " + demonstrateLengthEquality(
                new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(3.0, LengthUnit.FEET)));

        System.out.println("1 centimeter equals 0.393701 inches: " + demonstrateLengthEquality(
                new QuantityLength(1.0, LengthUnit.CENTIMETERS),
                new QuantityLength(0.393701, LengthUnit.INCHES)));

        System.out.println("1 foot in inches: "
                + demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES));

        QuantityLength yards = new QuantityLength(3.0, LengthUnit.YARDS);
        System.out.println("3 yards in feet: "
                + demonstrateLengthConversion(yards, LengthUnit.FEET));
        QuantityLength oneFoot = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength twelveInches = new QuantityLength(12.0, LengthUnit.INCHES);

        System.out.println("1 foot + 12 inches = "
                + demonstrateLengthAddition(oneFoot, twelveInches));

        System.out.println("12 inches + 1 foot = "
                + demonstrateLengthAddition(twelveInches, oneFoot));
        System.out.println("1 foot + 12 inches in inches = "
                + demonstrateLengthAddition(oneFoot, twelveInches, LengthUnit.INCHES));

        System.out.println("1 foot + 12 inches in yards = "
                + demonstrateLengthAddition(oneFoot, twelveInches, LengthUnit.YARDS));


// UC9: Weight measurement demonstrations

        QuantityWeight oneKilogram =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        QuantityWeight thousandGrams =
                new QuantityWeight(1000.0, WeightUnit.GRAM);

        System.out.println("1 kg equals 1000 g: "
                + demonstrateWeightEquality(
                oneKilogram, thousandGrams));

        System.out.println("1 kg converted to grams: "
                + demonstrateWeightConversion(
                oneKilogram, WeightUnit.GRAM));

        System.out.println("1 kg + 500 g = "
                + demonstrateWeightAddition(
                oneKilogram,
                new QuantityWeight(500.0, WeightUnit.GRAM),
                WeightUnit.KILOGRAM));

        System.out.println("1 kg + 500 g in grams = "
                + demonstrateWeightAddition(
                oneKilogram,
                new QuantityWeight(500.0, WeightUnit.GRAM),
                WeightUnit.GRAM));

    }
    }
