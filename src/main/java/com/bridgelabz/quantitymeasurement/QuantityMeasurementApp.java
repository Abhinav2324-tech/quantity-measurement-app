package com.bridgelabz.quantitymeasurement;

/**
 * UC 2 — Compares feet with feet and inches with inches.
 */
public class QuantityMeasurementApp {

    public static final class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            Feet other = (Feet) obj;
            return Double.compare(value, other.value) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }
    }

    public static final class Inches {
        private final double value;

        public Inches(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            Inches other = (Inches) obj;
            return Double.compare(value, other.value) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }
    }

    public static void demonstrateFeetEquality() {
        boolean equal = new Feet(1.0).equals(new Feet(1.0));
        System.out.println(equal ? "Feet: Equal (true)" : "Feet: Not equal (false)");
    }

    public static void demonstrateInchesEquality() {
        boolean equal = new Inches(1.0).equals(new Inches(1.0));
        System.out.println(equal ? "Inches: Equal (true)" : "Inches: Not equal (false)");
    }

    public static void main(String[] args) {
        demonstrateFeetEquality();
        demonstrateInchesEquality();
    }
}