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

    public static void main(String[] args) {
        demonstrateFeetEquality();
        demonstrateInchesEquality();
        demonstrateFeetInchesComparison();
    }
}