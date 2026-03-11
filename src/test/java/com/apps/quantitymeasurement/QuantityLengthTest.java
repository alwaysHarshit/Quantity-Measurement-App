package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityLengthTest {

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

    @Test
    void testAddition_SameUnit_FeetPlusFeet() {
        QuantityLength length1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength length2 = new QuantityLength(2.0, LengthUnit.FEET);
        QuantityLength result = QuantityLength.add(length1, length2);

        assertEquals(3.0, result.equals(new QuantityLength(3.0, LengthUnit.FEET)) ? 3.0 : 0.0, EPSILON);
        assertTrue(result.equals(new QuantityLength(3.0, LengthUnit.FEET)));
    }

    @Test
    void testAddition_SameUnit_InchPlusInch() {
        QuantityLength length1 = new QuantityLength(6.0, LengthUnit.INCHES);
        QuantityLength length2 = new QuantityLength(6.0, LengthUnit.INCHES);
        QuantityLength result = QuantityLength.add(length1, length2);

        assertTrue(result.equals(new QuantityLength(12.0, LengthUnit.INCHES)));
    }

    @Test
    void testAddition_CrossUnit_FeetPlusInches() {
        QuantityLength length1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength length2 = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength result = QuantityLength.add(length1, length2);

        assertTrue(result.equals(new QuantityLength(2.0, LengthUnit.FEET)));
    }

    @Test
    void testAddition_CrossUnit_InchPlusFeet() {
        QuantityLength length1 = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength length2 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength result = QuantityLength.add(length1, length2);

        assertTrue(result.equals(new QuantityLength(24.0, LengthUnit.INCHES)));
    }

    @Test
    void testAddition_CrossUnit_YardPlusFeet() {
        QuantityLength length1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength length2 = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength result = QuantityLength.add(length1, length2);

        assertTrue(result.equals(new QuantityLength(2.0, LengthUnit.YARDS)));
    }

    @Test
    void testAddition_CrossUnit_CentimeterPlusInch() {
        QuantityLength length1 = new QuantityLength(2.54, LengthUnit.CM);
        QuantityLength length2 = new QuantityLength(1.0, LengthUnit.INCHES);
        QuantityLength result = QuantityLength.add(length1, length2);

        assertTrue(result.equals(new QuantityLength(5.08, LengthUnit.CM)));
    }

    @Test
    void testAddition_Commutativity() {
        QuantityLength length1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength length2 = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result1 = QuantityLength.add(length1, length2);
        QuantityLength result2 = QuantityLength.add(length2, length1);

        assertTrue(result1.equals(result2));
    }

    @Test
    void testAddition_WithZero() {
        QuantityLength length1 = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength length2 = new QuantityLength(0.0, LengthUnit.INCHES);
        QuantityLength result = QuantityLength.add(length1, length2);

        assertTrue(result.equals(new QuantityLength(5.0, LengthUnit.FEET)));
    }

    @Test
    void testAddition_NegativeValues() {
        QuantityLength length1 = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength length2 = new QuantityLength(-2.0, LengthUnit.FEET);
        QuantityLength result = QuantityLength.add(length1, length2);

        assertTrue(result.equals(new QuantityLength(3.0, LengthUnit.FEET)));
    }


    @Test
    void testAddition_LargeValues() {
        QuantityLength length1 = new QuantityLength(1e6, LengthUnit.FEET);
        QuantityLength length2 = new QuantityLength(1e6, LengthUnit.FEET);
        QuantityLength result = QuantityLength.add(length1, length2);

        assertTrue(result.equals(new QuantityLength(2e6, LengthUnit.FEET)));
    }

    @Test
    void testAddition_SmallValues() {
        QuantityLength length1 = new QuantityLength(0.001, LengthUnit.FEET);
        QuantityLength length2 = new QuantityLength(0.002, LengthUnit.FEET);
        QuantityLength result = QuantityLength.add(length1, length2);

        assertTrue(result.equals(new QuantityLength(0.003, LengthUnit.FEET)));
    }
}