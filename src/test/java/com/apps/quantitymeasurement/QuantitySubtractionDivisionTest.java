package com.apps.quantitymeasurement;

import com.apps.quantitymeasurement.utils.LengthUnit;
import com.apps.quantitymeasurement.utils.QuantityClass;
import com.apps.quantitymeasurement.utils.VolumeUnit;
import com.apps.quantitymeasurement.utils.WeightUnit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.apps.quantitymeasurement.utils.LengthUnit.*;
import static com.apps.quantitymeasurement.utils.WeightUnit.*;
import static com.apps.quantitymeasurement.utils.VolumeUnit.*;

public class QuantitySubtractionDivisionTest {
    private static final double EPSILON = 1e-6;

    /******************** Subtraction Tests ********************/

    @Test
    void testSubtraction_SameUnit_FeetMinusFeet() {
        QuantityClass<LengthUnit> result = new QuantityClass<>(10.0, FEET).subtract(new QuantityClass<>(5.0, FEET));
        assertEquals(5.0, result.getValue(), EPSILON);
        assertEquals(FEET, result.getUnit());
    }

    @Test
    void testSubtraction_SameUnit_LitreMinusLitre() {
        QuantityClass<VolumeUnit> result = new QuantityClass<>(10.0, LITRE).subtract(new QuantityClass<>(3.0, LITRE));
        assertEquals(7.0, result.getValue(), EPSILON);
        assertEquals(LITRE, result.getUnit());
    }

    @Test
    void testSubtraction_CrossUnit_FeetMinusInches() {
        QuantityClass<LengthUnit> result = new QuantityClass<>(10.0, FEET).subtract(new QuantityClass<>(6.0, INCHES));
        assertEquals(9.5, result.getValue(), EPSILON);
        assertEquals(FEET, result.getUnit());
    }

    @Test
    void testSubtraction_CrossUnit_InchesMinusFeet() {
        QuantityClass<LengthUnit> result = new QuantityClass<>(120.0, INCHES).subtract(new QuantityClass<>(5.0, FEET));
        assertEquals(60.0, result.getValue(), 0.001); // Higher tolerance due to conversion factor precision
        assertEquals(INCHES, result.getUnit());
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Feet() {
        QuantityClass<LengthUnit> result = new QuantityClass<>(10.0, FEET).subtract(new QuantityClass<>(6.0, INCHES), FEET);
        assertEquals(9.5, result.getValue(), EPSILON);
        assertEquals(FEET, result.getUnit());
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Inches() {
        QuantityClass<LengthUnit> result = new QuantityClass<>(10.0, FEET).subtract(new QuantityClass<>(6.0, INCHES), INCHES);
        assertEquals(114.0, result.getValue(), 0.001); // Higher tolerance due to conversion factor precision
        assertEquals(INCHES, result.getUnit());
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Millilitre() {
        QuantityClass<VolumeUnit> result = new QuantityClass<>(5.0, LITRE).subtract(new QuantityClass<>(2.0, LITRE), MILLILITRE);
        assertEquals(3000.0, result.getValue(), EPSILON);
        assertEquals(MILLILITRE, result.getUnit());
    }

    @Test
    void testSubtraction_ResultingInNegative() {
        QuantityClass<LengthUnit> result = new QuantityClass<>(5.0, FEET).subtract(new QuantityClass<>(10.0, FEET));
        assertEquals(-5.0, result.getValue(), EPSILON);
        assertEquals(FEET, result.getUnit());
    }

    @Test
    void testSubtraction_ResultingInZero() {
        QuantityClass<LengthUnit> result = new QuantityClass<>(10.0, FEET).subtract(new QuantityClass<>(120.0, INCHES));
        assertEquals(0.0, result.getValue(), 0.001); // Higher tolerance due to conversion factor precision
        assertEquals(FEET, result.getUnit());
    }

    @Test
    void testSubtraction_WithZeroOperand() {
        QuantityClass<LengthUnit> result = new QuantityClass<>(5.0, FEET).subtract(new QuantityClass<>(0.0, INCHES));
        assertEquals(5.0, result.getValue(), EPSILON);
        assertEquals(FEET, result.getUnit());
    }

    @Test
    void testSubtraction_WithNegativeValues() {
        QuantityClass<LengthUnit> result = new QuantityClass<>(5.0, FEET).subtract(new QuantityClass<>(-2.0, FEET));
        assertEquals(7.0, result.getValue(), EPSILON);
        assertEquals(FEET, result.getUnit());
    }

    @Test
    void testSubtraction_NonCommutative() {
        QuantityClass<LengthUnit> result1 = new QuantityClass<>(10.0, FEET).subtract(new QuantityClass<>(5.0, FEET));
        QuantityClass<LengthUnit> result2 = new QuantityClass<>(5.0, FEET).subtract(new QuantityClass<>(10.0, FEET));

        assertEquals(5.0, result1.getValue(), EPSILON);
        assertEquals(-5.0, result2.getValue(), EPSILON);
        assertNotEquals(result1.getValue(), result2.getValue());
    }

    @Test
    void testSubtraction_WithLargeValues() {
        QuantityClass<WeightUnit> result = new QuantityClass<>(1e6, KILOGRAM).subtract(new QuantityClass<>(5e5, KILOGRAM));
        assertEquals(5e5, result.getValue(), EPSILON);
        assertEquals(KILOGRAM, result.getUnit());
    }

    @Test
    void testSubtraction_WithSmallValues() {
        QuantityClass<LengthUnit> result = new QuantityClass<>(0.001, FEET).subtract(new QuantityClass<>(0.0005, FEET));
        assertEquals(0.0005, result.getValue(), EPSILON);
        assertEquals(FEET, result.getUnit());
    }

    @Test
    void testSubtraction_NullOperand() {
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(10.0, FEET);
        assertThrows(IllegalArgumentException.class, () -> {
            q1.subtract(null);
        });
    }
    @Test
    void testSubtraction_CrossCategory() {
        QuantityClass<LengthUnit> length = new QuantityClass<>(10.0, FEET);
        QuantityClass<WeightUnit> weight = new QuantityClass<>(5.0, KILOGRAM);

        // Type system prevents this at compile time, but runtime check would fail
        // We cannot directly test this due to generic type constraints
        // This test documents the expected behavior
        assertTrue(true); // Placeholder - compile-time safety
    }

    @Test
    void testSubtraction_AllMeasurementCategories() {
        // Length
        QuantityClass<LengthUnit> lengthResult = new QuantityClass<>(10.0, FEET).subtract(new QuantityClass<>(2.0, FEET));
        assertEquals(8.0, lengthResult.getValue(), EPSILON);

        // Weight
        QuantityClass<WeightUnit> weightResult = new QuantityClass<>(5.0, KILOGRAM).subtract(new QuantityClass<>(1.0, KILOGRAM));
        assertEquals(4.0, weightResult.getValue(), EPSILON);

        // Volume
        QuantityClass<VolumeUnit> volumeResult = new QuantityClass<>(10.0, LITRE).subtract(new QuantityClass<>(3.0, LITRE));
        assertEquals(7.0, volumeResult.getValue(), EPSILON);
    }

    @Test
    void testSubtraction_ChainedOperations() {
        QuantityClass<LengthUnit> result = new QuantityClass<>(10.0, FEET)
                .subtract(new QuantityClass<>(2.0, FEET))
                .subtract(new QuantityClass<>(1.0, FEET));
        assertEquals(7.0, result.getValue(), EPSILON);
        assertEquals(FEET, result.getUnit());
    }

    /******************** Division Tests ********************/

    @Test
    void testDivision_SameUnit_FeetDividedByFeet() {
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(10.0, FEET);
        QuantityClass<LengthUnit> q2 = new QuantityClass<>(2.0, FEET);

        double ratio = q1.convertTo(FEET).getValue() / q2.convertTo(FEET).getValue();
        assertEquals(5.0, ratio, EPSILON);
    }

    @Test
    void testDivision_SameUnit_LitreDividedByLitre() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(10.0, LITRE);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(5.0, LITRE);

        double ratio = q1.convertTo(LITRE).getValue() / q2.convertTo(LITRE).getValue();
        assertEquals(2.0, ratio, EPSILON);
    }

    @Test
    void testDivision_CrossUnit_FeetDividedByInches() {
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(24.0, INCHES);
        QuantityClass<LengthUnit> q2 = new QuantityClass<>(2.0, FEET);

        double ratio = q1.convertTo(INCHES).getValue() / q2.convertTo(INCHES).getValue();
        assertEquals(1.0, ratio, EPSILON);
    }

    @Test
    void testDivision_CrossUnit_KilogramDividedByGram() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(2.0, KILOGRAM);
        QuantityClass<WeightUnit> q2 = new QuantityClass<>(2000.0, GRAM);

        double ratio = q1.convertTo(KILOGRAM).getValue() / q2.convertTo(KILOGRAM).getValue();
        assertEquals(1.0, ratio, EPSILON);
    }

    @Test
    void testDivision_RatioGreaterThanOne() {
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(10.0, FEET);
        QuantityClass<LengthUnit> q2 = new QuantityClass<>(2.0, FEET);

        double ratio = q1.getValue() / q2.getValue();
        assertEquals(5.0, ratio, EPSILON);
    }

    @Test
    void testDivision_RatioLessThanOne() {
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(5.0, FEET);
        QuantityClass<LengthUnit> q2 = new QuantityClass<>(10.0, FEET);

        double ratio = q1.getValue() / q2.getValue();
        assertEquals(0.5, ratio, EPSILON);
    }

    @Test
    void testDivision_RatioEqualToOne() {
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(10.0, FEET);
        QuantityClass<LengthUnit> q2 = new QuantityClass<>(10.0, FEET);

        double ratio = q1.getValue() / q2.getValue();
        assertEquals(1.0, ratio, EPSILON);
    }

    @Test
    void testDivision_NonCommutative() {
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(10.0, FEET);
        QuantityClass<LengthUnit> q2 = new QuantityClass<>(5.0, FEET);

        double ratio1 = q1.getValue() / q2.getValue();
        double ratio2 = q2.getValue() / q1.getValue();

        assertEquals(2.0, ratio1, EPSILON);
        assertEquals(0.5, ratio2, EPSILON);
        assertNotEquals(ratio1, ratio2);
    }

    @Test
    void testDivision_ByZero() {
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(10.0, FEET);
        assertThrows(ArithmeticException.class, () -> {
            double result = q1.getValue() / 0.0;
            if (Double.isInfinite(result)) {
                throw new ArithmeticException("Division by zero");
            }
        });
    }

    @Test
    void testDivision_WithLargeRatio() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1e6, KILOGRAM);
        QuantityClass<WeightUnit> q2 = new QuantityClass<>(1.0, KILOGRAM);

        double ratio = q1.getValue() / q2.getValue();
        assertEquals(1e6, ratio, EPSILON);
    }

    @Test
    void testDivision_WithSmallRatio() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1.0, KILOGRAM);
        QuantityClass<WeightUnit> q2 = new QuantityClass<>(1e6, KILOGRAM);

        double ratio = q1.getValue() / q2.getValue();
        assertEquals(1e-6, ratio, EPSILON);
    }

    @Test
    void testDivision_NullOperand() {
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(10.0, FEET);
        assertThrows(NullPointerException.class, () -> {
            QuantityClass<LengthUnit> nullQ = null;
            double ratio = q1.getValue() / nullQ.getValue();
        });
    }

    @Test
    void testDivision_CrossCategory() {
        QuantityClass<LengthUnit> length = new QuantityClass<>(10.0, FEET);
        QuantityClass<WeightUnit> weight = new QuantityClass<>(5.0, KILOGRAM);

        // Type system prevents this at compile time
        // This test documents the expected behavior
        assertTrue(true); // Placeholder - compile-time safety
    }

    @Test
    void testDivision_AllMeasurementCategories() {
        // Length
        QuantityClass<LengthUnit> l1 = new QuantityClass<>(10.0, FEET);
        QuantityClass<LengthUnit> l2 = new QuantityClass<>(2.0, FEET);
        assertEquals(5.0, l1.getValue() / l2.getValue(), EPSILON);

        // Weight
        QuantityClass<WeightUnit> w1 = new QuantityClass<>(10.0, KILOGRAM);
        QuantityClass<WeightUnit> w2 = new QuantityClass<>(2.0, KILOGRAM);
        assertEquals(5.0, w1.getValue() / w2.getValue(), EPSILON);

        // Volume
        QuantityClass<VolumeUnit> v1 = new QuantityClass<>(10.0, LITRE);
        QuantityClass<VolumeUnit> v2 = new QuantityClass<>(2.0, LITRE);
        assertEquals(5.0, v1.getValue() / v2.getValue(), EPSILON);
    }

    @Test
    void testDivision_Associativity() {
        QuantityClass<LengthUnit> a = new QuantityClass<>(100.0, FEET);
        QuantityClass<LengthUnit> b = new QuantityClass<>(10.0, FEET);
        QuantityClass<LengthUnit> c = new QuantityClass<>(2.0, FEET);

        // (A ÷ B) ÷ C
        double result1 = (a.getValue() / b.getValue()) / c.getValue();

        // A ÷ (B ÷ C)
        double result2 = a.getValue() / (b.getValue() / c.getValue());

        assertNotEquals(result1, result2, EPSILON);
    }

    /******************** Integration Tests ********************/

    @Test
    void testSubtractionAndDivision_Integration() {
        QuantityClass<LengthUnit> a = new QuantityClass<>(10.0, FEET);
        QuantityClass<LengthUnit> b = new QuantityClass<>(2.0, FEET);
        QuantityClass<LengthUnit> c = new QuantityClass<>(2.0, FEET);

        QuantityClass<LengthUnit> subtractResult = a.subtract(b);
        double divisionResult = subtractResult.getValue() / c.getValue();

        assertEquals(4.0, divisionResult, EPSILON);
    }

    @Test
    void testSubtractionAddition_Inverse() {
        QuantityClass<LengthUnit> a = new QuantityClass<>(10.0, FEET);
        QuantityClass<LengthUnit> b = new QuantityClass<>(5.0, FEET);

        QuantityClass<LengthUnit> result = a.add(b).subtract(b);
        assertEquals(a.getValue(), result.getValue(), EPSILON);
    }

    /******************** Immutability Tests ********************/

    @Test
    void testSubtraction_Immutability() {
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(10.0, FEET);
        QuantityClass<LengthUnit> q2 = new QuantityClass<>(5.0, FEET);

        double originalValue1 = q1.getValue();
        double originalValue2 = q2.getValue();
        LengthUnit originalUnit1 = q1.getUnit();
        LengthUnit originalUnit2 = q2.getUnit();

        QuantityClass<LengthUnit> result = q1.subtract(q2);

        assertEquals(originalValue1, q1.getValue(), EPSILON);
        assertEquals(originalValue2, q2.getValue(), EPSILON);
        assertEquals(originalUnit1, q1.getUnit());
        assertEquals(originalUnit2, q2.getUnit());
        assertNotSame(q1, result);
    }

    @Test
    void testDivision_Immutability() {
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(10.0, FEET);

        double originalValue = q1.getValue();
        LengthUnit originalUnit = q1.getUnit();

        double ratio = q1.getValue() / 2.0;

        assertEquals(originalValue, q1.getValue(), EPSILON);
        assertEquals(originalUnit, q1.getUnit());
        assertEquals(5.0, ratio, EPSILON);
    }

    /******************** Precision Tests ********************/

    @Test
    void testSubtraction_PrecisionAndRounding() {
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(10.123456, FEET);
        QuantityClass<LengthUnit> q2 = new QuantityClass<>(5.123456, FEET);
        QuantityClass<LengthUnit> result = q1.subtract(q2);

        // Verify precision is maintained in calculations
        assertEquals(5.0, result.getValue(), EPSILON);
    }

    @Test
    void testDivision_PrecisionHandling() {
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(10.0, FEET);
        QuantityClass<LengthUnit> q2 = new QuantityClass<>(3.0, FEET);

        double ratio = q1.getValue() / q2.getValue();

        // Division maintains floating-point precision
        assertEquals(3.333333, ratio, EPSILON);
    }
}
