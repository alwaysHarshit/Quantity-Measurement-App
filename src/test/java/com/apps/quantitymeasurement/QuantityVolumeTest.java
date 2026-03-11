package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityVolumeTest {
    private static final double EPSILON = 1e-6;

    // Equality Tests - Same Unit
    @Test
    void testEquality_LitreToLitre_SameValue() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(1.0, VolumeUnit.LITER);
        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_LitreToLitre_DifferentValue() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(2.0, VolumeUnit.LITER);
        assertFalse(q1.equals(q2));
    }

    // Equality Tests - Cross Unit
    @Test
    void testEquality_LitreToMillilitre_EquivalentValue() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(1000.0, VolumeUnit.MILLILITRE);
        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_MillilitreToLitre_EquivalentValue() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1000.0, VolumeUnit.MILLILITRE);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(1.0, VolumeUnit.LITER);
        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_LitreToGallon_EquivalentValue() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(0.264172, VolumeUnit.GALLON);
        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_GallonToLitre_EquivalentValue() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1.0, VolumeUnit.GALLON);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(3.78541, VolumeUnit.LITER);
        assertTrue(q1.equals(q2));
    }

    // Cross-Category Incompatibility
    @Test
    void testEquality_VolumeVsLength_Incompatible() {
        QuantityClass<VolumeUnit> volume = new QuantityClass<>(1.0, VolumeUnit.LITER);
        QuantityClass<LengthUnit> length = new QuantityClass<>(1.0, LengthUnit.FEET);
        assertFalse(volume.equals(length));
    }

    @Test
    void testEquality_VolumeVsWeight_Incompatible() {
        QuantityClass<VolumeUnit> volume = new QuantityClass<>(1.0, VolumeUnit.LITER);
        QuantityClass<WeightUnit> weight = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        assertFalse(volume.equals(weight));
    }

    // Null and Reference Tests
    @Test
    void testEquality_NullComparison() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1.0, VolumeUnit.LITER);
        assertFalse(q1.equals(null));
    }

    @Test
    void testEquality_SameReference() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1.0, VolumeUnit.LITER);
        assertTrue(q1.equals(q1));
    }

    @Test
    void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> {
            new QuantityClass<VolumeUnit>(1.0, null);
        });
    }

    // Mathematical Properties
    @Test
    void testEquality_TransitiveProperty() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(1000.0, VolumeUnit.MILLILITRE);
        QuantityClass<VolumeUnit> q3 = new QuantityClass<>(1.0, VolumeUnit.LITER);
        assertTrue(q1.equals(q2) && q2.equals(q3) && q1.equals(q3));
    }

    // Edge Cases - Values
    @Test
    void testEquality_ZeroValue() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(0.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(0.0, VolumeUnit.MILLILITRE);
        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_NegativeVolume() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(-1.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(-1000.0, VolumeUnit.MILLILITRE);
        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_LargeVolumeValue() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1000000.0, VolumeUnit.MILLILITRE);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(1000.0, VolumeUnit.LITER);
        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_SmallVolumeValue() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(0.001, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(1.0, VolumeUnit.MILLILITRE);
        assertTrue(q1.equals(q2));
    }

    // Conversion Tests - Basic
    @Test
    void testConversion_LitreToMillilitre() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> result = q1.convertTo(VolumeUnit.MILLILITRE);
        assertEquals(1000.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.MILLILITRE, result.getUnit());
    }

    @Test
    void testConversion_MillilitreToLitre() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1000.0, VolumeUnit.MILLILITRE);
        QuantityClass<VolumeUnit> result = q1.convertTo(VolumeUnit.LITER);
        assertEquals(1.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITER, result.getUnit());
    }

    @Test
    void testConversion_GallonToLitre() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1.0, VolumeUnit.GALLON);
        QuantityClass<VolumeUnit> result = q1.convertTo(VolumeUnit.LITER);
        assertEquals(3.78541, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITER, result.getUnit());
    }

    @Test
    void testConversion_LitreToGallon() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(3.78541, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> result = q1.convertTo(VolumeUnit.GALLON);
        assertEquals(1.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.GALLON, result.getUnit());
    }

    @Test
    void testConversion_MillilitreToGallon() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1000.0, VolumeUnit.MILLILITRE);
        QuantityClass<VolumeUnit> result = q1.convertTo(VolumeUnit.GALLON);
        assertEquals(0.264172, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.GALLON, result.getUnit());
    }

    @Test
    void testConversion_SameUnit() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(5.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> result = q1.convertTo(VolumeUnit.LITER);
        assertEquals(5.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITER, result.getUnit());
    }

    // Conversion Tests - Edge Cases
    @Test
    void testConversion_ZeroValue() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(0.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> result = q1.convertTo(VolumeUnit.MILLILITRE);
        assertEquals(0.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.MILLILITRE, result.getUnit());
    }

    @Test
    void testConversion_NegativeValue() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(-1.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> result = q1.convertTo(VolumeUnit.MILLILITRE);
        assertEquals(-1000.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.MILLILITRE, result.getUnit());
    }

    @Test
    void testConversion_RoundTrip() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1.5, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> result = q1.convertTo(VolumeUnit.MILLILITRE).convertTo(VolumeUnit.LITER);
        assertEquals(1.5, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITER, result.getUnit());
    }

    // Addition Tests - Same Unit
    @Test
    void testAddition_SameUnit_LitrePlusLitre() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(2.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> result = q1.add(q2);
        assertEquals(3.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITER, result.getUnit());
    }

    @Test
    void testAddition_SameUnit_MillilitrePlusMillilitre() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(500.0, VolumeUnit.MILLILITRE);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(500.0, VolumeUnit.MILLILITRE);
        QuantityClass<VolumeUnit> result = q1.add(q2);
        assertEquals(1000.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.MILLILITRE, result.getUnit());
    }

    // Addition Tests - Cross Unit
    @Test
    void testAddition_CrossUnit_LitrePlusMillilitre() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(1000.0, VolumeUnit.MILLILITRE);
        QuantityClass<VolumeUnit> result = q1.add(q2);
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITER, result.getUnit());
    }

    @Test
    void testAddition_CrossUnit_MillilitrePlusLitre() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1000.0, VolumeUnit.MILLILITRE);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(1.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> result = q1.add(q2);
        assertEquals(2000.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.MILLILITRE, result.getUnit());
    }

    @Test
    void testAddition_CrossUnit_GallonPlusLitre() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1.0, VolumeUnit.GALLON);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(3.78541, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> result = q1.add(q2);
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.GALLON, result.getUnit());
    }

    // Addition Tests - Explicit Target Unit
    @Test
    void testAddition_ExplicitTargetUnit_Litre() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(1000.0, VolumeUnit.MILLILITRE);
        QuantityClass<VolumeUnit> result = q1.add(q2, VolumeUnit.LITER);
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITER, result.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Millilitre() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(1000.0, VolumeUnit.MILLILITRE);
        QuantityClass<VolumeUnit> result = q1.add(q2, VolumeUnit.MILLILITRE);
        assertEquals(2000.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.MILLILITRE, result.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Gallon() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(3.78541, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(3.78541, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> result = q1.add(q2, VolumeUnit.GALLON);
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.GALLON, result.getUnit());
    }

    // Addition Tests - Mathematical Properties
    @Test
    void testAddition_Commutativity() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(1000.0, VolumeUnit.MILLILITRE);
        
        QuantityClass<VolumeUnit> result1 = q1.add(q2, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> result2 = q2.add(q1, VolumeUnit.LITER);
        
        assertEquals(result1.getValue(), result2.getValue(), EPSILON);
        assertEquals(result1.getUnit(), result2.getUnit());
    }

    // Addition Tests - Edge Cases
    @Test
    void testAddition_WithZero() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(5.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(0.0, VolumeUnit.MILLILITRE);
        QuantityClass<VolumeUnit> result = q1.add(q2);
        assertEquals(5.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITER, result.getUnit());
    }

    @Test
    void testAddition_NegativeValues() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(5.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(-2000.0, VolumeUnit.MILLILITRE);
        QuantityClass<VolumeUnit> result = q1.add(q2);
        assertEquals(3.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITER, result.getUnit());
    }

    @Test
    void testAddition_LargeValues() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(1e6, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(1e6, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> result = q1.add(q2);
        assertEquals(2e6, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITER, result.getUnit());
    }

    @Test
    void testAddition_SmallValues() {
        QuantityClass<VolumeUnit> q1 = new QuantityClass<>(0.001, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> q2 = new QuantityClass<>(0.002, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> result = q1.add(q2);
        assertEquals(0.003, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.LITER, result.getUnit());
    }

    // VolumeUnit Enum Tests
    @Test
    void testVolumeUnitEnum_LitreConstant() {
        assertEquals(1.0, VolumeUnit.LITER.getConversionFactor(), EPSILON);
    }

    @Test
    void testVolumeUnitEnum_MillilitreConstant() {
        assertEquals(0.001, VolumeUnit.MILLILITRE.getConversionFactor(), EPSILON);
    }

    @Test
    void testVolumeUnitEnum_GallonConstant() {
        assertEquals(3.78541, VolumeUnit.GALLON.getConversionFactor(), EPSILON);
    }

    // VolumeUnit Enum Methods - convertToBaseUnit
    @Test
    void testConvertToBaseUnit_LitreToLitre() {
        double result = VolumeUnit.LITER.convertToBaseUnit(5.0);
        assertEquals(5.0, result, EPSILON);
    }

    @Test
    void testConvertToBaseUnit_MillilitreToLitre() {
        double result = VolumeUnit.MILLILITRE.convertToBaseUnit(1000.0);
        assertEquals(1.0, result, EPSILON);
    }

    @Test
    void testConvertToBaseUnit_GallonToLitre() {
        double result = VolumeUnit.GALLON.convertToBaseUnit(1.0);
        assertEquals(3.78541, result, EPSILON);
    }

    // VolumeUnit Enum Methods - convertFromBaseUnit
    @Test
    void testConvertFromBaseUnit_LitreToLitre() {
        double result = VolumeUnit.LITER.convertFromBaseUnit(2.0);
        assertEquals(2.0, result, EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_LitreToMillilitre() {
        double result = VolumeUnit.MILLILITRE.convertFromBaseUnit(1.0);
        assertEquals(1000.0, result, EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_LitreToGallon() {
        double result = VolumeUnit.GALLON.convertFromBaseUnit(3.78541);
        assertEquals(1.0, result, EPSILON);
    }

    // Backward Compatibility Tests
    @Test
    void testBackwardCompatibility_AllUC1Through10Tests() {
        // Verify length functionality still works
        QuantityClass<LengthUnit> length1 = new QuantityClass<>(1.0, LengthUnit.FEET);
        QuantityClass<LengthUnit> length2 = new QuantityClass<>(12.0, LengthUnit.INCHES);
        assertTrue(length1.equals(length2));
        
        // Verify weight functionality still works
        QuantityClass<WeightUnit> weight1 = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> weight2 = new QuantityClass<>(1000.0, WeightUnit.GRAM);
        assertTrue(weight1.equals(weight2));
    }

    // Generic Quantity Consistency
    @Test
    void testGenericQuantity_VolumeOperations_Consistency() {
        // Volume works the same way as length and weight
        QuantityClass<VolumeUnit> volume = new QuantityClass<>(1.0, VolumeUnit.LITER);
        assertNotNull(volume.getValue());
        assertNotNull(volume.getUnit());
        assertNotNull(volume.toString());
    }

    // Scalability Integration
    @Test
    void testScalability_VolumeIntegration() {
        // Volume integrates seamlessly without modifying existing code
        QuantityClass<VolumeUnit> volume = new QuantityClass<>(1.0, VolumeUnit.LITER);
        QuantityClass<VolumeUnit> converted = volume.convertTo(VolumeUnit.MILLILITRE);
        QuantityClass<VolumeUnit> added = volume.add(converted);
        
        assertNotNull(volume);
        assertNotNull(converted);
        assertNotNull(added);
        assertEquals(VolumeUnit.LITER, added.getUnit());
    }
}
