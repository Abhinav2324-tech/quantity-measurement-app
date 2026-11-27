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


    /**
     * UC10: Generic equality demonstration.
     */
    public static <U extends IMeasurable> boolean demonstrateEquality(
            Quantity<U> first,
            Quantity<U> second) {

        return first.equals(second);
    }

    /**
     * UC10: Generic conversion demonstration.
     */
    public static <U extends IMeasurable> Quantity<U> demonstrateConversion(
            Quantity<U> quantity,
            U targetUnit) {

        return quantity.convertTo(targetUnit);
    }

    /**
     * UC10: Generic addition demonstration.
     */
    public static <U extends IMeasurable> Quantity<U> demonstrateAddition(
            Quantity<U> first,
            Quantity<U> second,
            U targetUnit) {

        return first.add(second, targetUnit);
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

// UC10: Generic Quantity demonstrations

        Quantity<LengthUnit> length1 =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> length2 =
                new Quantity<>(12.0, LengthUnit.INCHES);

        System.out.println("Generic Length Equality: "
                + demonstrateEquality(length1, length2));

        System.out.println("Generic Length Addition: "
                + demonstrateAddition(
                length1, length2, LengthUnit.INCHES));

        Quantity<WeightUnit> weight1 =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> weight2 =
                new Quantity<>(500.0, WeightUnit.GRAM);

        System.out.println("Generic Weight Addition: "
                + demonstrateAddition(
                weight1, weight2, WeightUnit.KILOGRAM));

        System.out.println("Generic Weight Conversion: "
                + demonstrateConversion(
                weight1, WeightUnit.GRAM));

// UC11: Volume Measurement Demonstration

        Quantity<VolumeUnit> oneLitre =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> thousandMillilitres =
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        Quantity<VolumeUnit> oneGallon =
                new Quantity<>(1.0, VolumeUnit.GALLON);

// Equality
        System.out.println("1 litre equals 1000 millilitres: "
                + demonstrateEquality(oneLitre, thousandMillilitres));

// Conversion
        System.out.println("1 litre in millilitres: "
                + demonstrateConversion(oneLitre, VolumeUnit.MILLILITRE));

        System.out.println("1 gallon in litres: "
                + demonstrateConversion(oneGallon, VolumeUnit.LITRE));

// Addition using first operand's unit
        System.out.println("1 litre + 1000 millilitres: "
                + oneLitre.add(thousandMillilitres));

// Addition with explicit target unit
        System.out.println("1 litre + 1 gallon in millilitres: "
                + demonstrateAddition(
                oneLitre, oneGallon, VolumeUnit.MILLILITRE));

    }
    }
