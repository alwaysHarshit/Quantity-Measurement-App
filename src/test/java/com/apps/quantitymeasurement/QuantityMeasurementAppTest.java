package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityLengthTest {

    private static final double EPSILON = 1e-6;

    @Test
    void testConversion_FeetToInches() {
        double result = QuantityLength.convert(1.0, LengthUnit.FEET, LengthUnit.INCHES);
        assertEquals(12.0, result, EPSILON);
    }

    @Test
    void testConversion_InchesToFeet() {
        double result = QuantityLength.convert(24.0, LengthUnit.INCHES, LengthUnit.FEET);
        assertEquals(2.0, result, EPSILON);
    }

    @Test
    void testConversion_YardsToInches() {
        double result = QuantityLength.convert(1.0, LengthUnit.YARDS, LengthUnit.INCHES);
        assertEquals(36.0, result, EPSILON);
    }

    @Test
    void testConversion_InchesToYards() {
        double result = QuantityLength.convert(72.0, LengthUnit.INCHES, LengthUnit.YARDS);
        assertEquals(2.0, result, EPSILON);
    }

    @Test
    void testConversion_CentimetersToInches() {
        double result = QuantityLength.convert(2.54, LengthUnit.CM, LengthUnit.INCHES);
        assertEquals(1.0, result, EPSILON);
    }

    @Test
    void testConversion_FeetToYard() {
        double result = QuantityLength.convert(6.0, LengthUnit.FEET, LengthUnit.YARDS);
        assertEquals(2.0, result, EPSILON);
    }

    @Test
    void testConversion_RoundTrip_PreservesValue() {
        double v = 5.0;

        double converted = QuantityLength.convert(v, LengthUnit.FEET, LengthUnit.INCHES);
        double roundTrip = QuantityLength.convert(converted, LengthUnit.INCHES, LengthUnit.FEET);

        assertEquals(v, roundTrip, EPSILON);
    }

    @Test
    void testConversion_ZeroValue() {
        double result = QuantityLength.convert(0.0, LengthUnit.FEET, LengthUnit.INCHES);
        assertEquals(0.0, result, EPSILON);
    }

    @Test
    void testConversion_NegativeValue() {
        double result = QuantityLength.convert(-1.0, LengthUnit.FEET, LengthUnit.INCHES);
        assertEquals(-12.0, result, EPSILON);
    }

    @Test
    void testConversion_InvalidUnit_Throws() {
        assertThrows(IllegalArgumentException.class, () ->
                QuantityLength.convert(1.0, null, LengthUnit.INCHES)
        );
    }

    @Test
    void testConversion_NaNOrInfinite_Throws() {

        assertThrows(IllegalArgumentException.class, () ->
                QuantityLength.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCHES)
        );

        assertThrows(IllegalArgumentException.class, () ->
                QuantityLength.convert(Double.POSITIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCHES)
        );

        assertThrows(IllegalArgumentException.class, () ->
                QuantityLength.convert(Double.NEGATIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCHES)
        );
    }
}