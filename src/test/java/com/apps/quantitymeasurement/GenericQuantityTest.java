package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GenericQuantityTest {
    private static final double EPSILON = 1e-6;

    // IMeasurable Interface Implementation Tests
    @Test
    void testIMeasurableInterface_LengthUnitImplementation() {
        assertTrue(IMeasurable.class.isAssignableFrom(LengthUnit.class));
        LengthUnit feet = LengthUnit.FEET;
        assertEquals(1.0, feet.getConversionFactor(), EPSILON);
        assertEquals(12.0, feet.convertToBaseUnit(12.0), EPSILON);
        assertEquals(12.0, feet.convertFromBaseUnit(12.0), EPSILON);
        assertEquals("FEET", feet.getUnitName());
    }

    @Test
    void testIMeasurableInterface_WeightUnitImplementation() {
        assertTrue(IMeasurable.class.isAssignableFrom(WeightUnit.class));
        WeightUnit kilogram = WeightUnit.KILOGRAM;
        assertEquals(1.0, kilogram.getConversionFactor(), EPSILON);
        assertEquals(1.0, kilogram.convertToBaseUnit(1.0), EPSILON);
        assertEquals(1.0, kilogram.convertFromBaseUnit(1.0), EPSILON);
        assertEquals("KILOGRAM", kilogram.getUnitName());
    }

    @Test
    void testIMeasurableInterface_ConsistentBehavior() {
        // Both enums implement the same interface methods
        IMeasurable lengthUnit = LengthUnit.INCHES;
        IMeasurable weightUnit = WeightUnit.GRAM;
        
        assertNotNull(lengthUnit.getConversionFactor());
        assertNotNull(weightUnit.getConversionFactor());
        assertNotNull(lengthUnit.getUnitName());
        assertNotNull(weightUnit.getUnitName());
    }

    // Generic QuantityClass Operations - Length
    @Test
    void testGenericQuantity_LengthOperations_Equality() {
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(1.0, LengthUnit.FEET);
        QuantityClass<LengthUnit> q2 = new QuantityClass<>(12.0, LengthUnit.INCHES);
        assertTrue(q1.equals(q2));
    }

    @Test
    void testGenericQuantity_WeightOperations_Equality() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> q2 = new QuantityClass<>(1000.0, WeightUnit.GRAM);
        assertTrue(q1.equals(q2));
    }

    @Test
    void testGenericQuantity_LengthOperations_Conversion() {
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(1.0, LengthUnit.FEET);
        QuantityClass<LengthUnit> result = q1.convertTo(LengthUnit.INCHES);
        assertEquals(12.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }

    @Test
    void testGenericQuantity_WeightOperations_Conversion() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> result = q1.convertTo(WeightUnit.GRAM);
        assertEquals(1000.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.GRAM, result.getUnit());
    }

    @Test
    void testGenericQuantity_LengthOperations_Addition() {
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(1.0, LengthUnit.FEET);
        QuantityClass<LengthUnit> q2 = new QuantityClass<>(12.0, LengthUnit.INCHES);
        QuantityClass<LengthUnit> result = q1.add(q2, LengthUnit.FEET);
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testGenericQuantity_WeightOperations_Addition() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> q2 = new QuantityClass<>(1000.0, WeightUnit.GRAM);
        QuantityClass<WeightUnit> result = q1.add(q2, WeightUnit.KILOGRAM);
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    // Cross-Category Prevention
    @Test
    void testCrossCategoryPrevention_LengthVsWeight() {
        QuantityClass<LengthUnit> length = new QuantityClass<>(1.0, LengthUnit.FEET);
        QuantityClass<WeightUnit> weight = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        assertFalse(length.equals(weight));
    }

    // Constructor Validation
    @Test
    void testGenericQuantity_ConstructorValidation_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> {
            new QuantityClass<LengthUnit>(1.0, null);
        });
    }

    @Test
    void testGenericQuantity_ConstructorValidation_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            new QuantityClass<>(Double.NaN, LengthUnit.FEET);
        });
    }

    // Conversion Tests - All Unit Combinations
    @Test
    void testGenericQuantity_Conversion_AllUnitCombinations_Length() {
        QuantityClass<LengthUnit> feet = new QuantityClass<>(1.0, LengthUnit.FEET);
        
        QuantityClass<LengthUnit> toInches = feet.convertTo(LengthUnit.INCHES);
        assertEquals(12.0, toInches.getValue(), EPSILON);
        
        QuantityClass<LengthUnit> toYards = feet.convertTo(LengthUnit.YARDS);
        assertEquals(0.333333, toYards.getValue(), EPSILON);
        
        QuantityClass<LengthUnit> toCm = feet.convertTo(LengthUnit.CM);
        assertEquals(30.48, toCm.getValue(), 0.01);
    }

    @Test
    void testGenericQuantity_Conversion_AllUnitCombinations_Weight() {
        QuantityClass<WeightUnit> kg = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        
        QuantityClass<WeightUnit> toGram = kg.convertTo(WeightUnit.GRAM);
        assertEquals(1000.0, toGram.getValue(), EPSILON);
        
        QuantityClass<WeightUnit> toPound = kg.convertTo(WeightUnit.POUND);
        assertEquals(2.20462, toPound.getValue(), EPSILON);
    }

    // Addition Tests - All Unit Combinations
    @Test
    void testGenericQuantity_Addition_AllUnitCombinations() {
        QuantityClass<LengthUnit> feet = new QuantityClass<>(1.0, LengthUnit.FEET);
        QuantityClass<LengthUnit> inches = new QuantityClass<>(6.0, LengthUnit.INCHES);
        
        QuantityClass<LengthUnit> result = feet.add(inches, LengthUnit.INCHES);
        assertEquals(18.0, result.getValue(), EPSILON);
    }

    // Backward Compatibility Tests
    @Test
    void testBackwardCompatibility_LengthEquality() {
        // Original: new QuantityLength(1.0, FEET).equals(new QuantityLength(12.0, INCHES))
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(1.0, LengthUnit.FEET);
        QuantityClass<LengthUnit> q2 = new QuantityClass<>(12.0, LengthUnit.INCHES);
        assertTrue(q1.equals(q2));
    }

    @Test
    void testBackwardCompatibility_WeightEquality() {
        // Original: new QuantityWeight(1.0, KILOGRAM).equals(new QuantityWeight(1000.0, GRAM))
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> q2 = new QuantityClass<>(1000.0, WeightUnit.GRAM);
        assertTrue(q1.equals(q2));
    }

    @Test
    void testBackwardCompatibility_LengthConversion() {
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(1.0, LengthUnit.FEET);
        QuantityClass<LengthUnit> result = q1.convertTo(LengthUnit.INCHES);
        assertEquals(12.0, result.getValue(), EPSILON);
    }

    @Test
    void testBackwardCompatibility_WeightConversion() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> result = q1.convertTo(WeightUnit.GRAM);
        assertEquals(1000.0, result.getValue(), EPSILON);
    }

    @Test
    void testBackwardCompatibility_LengthAddition() {
        QuantityClass<LengthUnit> q1 = new QuantityClass<>(1.0, LengthUnit.FEET);
        QuantityClass<LengthUnit> q2 = new QuantityClass<>(1.0, LengthUnit.FEET);
        QuantityClass<LengthUnit> result = q1.add(q2);
        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    void testBackwardCompatibility_WeightAddition() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> q2 = new QuantityClass<>(1000.0, WeightUnit.GRAM);
        QuantityClass<WeightUnit> result = q1.add(q2);
        assertEquals(2.0, result.getValue(), EPSILON);
    }

    // Type Wildcard Tests
    @Test
    void testTypeWildcard_FlexibleSignatures() {
        QuantityClass<LengthUnit> length = new QuantityClass<>(1.0, LengthUnit.FEET);
        QuantityClass<WeightUnit> weight = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        
        // Both work with QuantityClass<?>
        QuantityClass<?> wildcard1 = length;
        QuantityClass<?> wildcard2 = weight;
        
        assertNotNull(wildcard1.getValue());
        assertNotNull(wildcard2.getValue());
    }

    // HashCode Tests
    @Test
    void testHashCode_GenericQuantity_Consistency() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> q2 = new QuantityClass<>(1000.0, WeightUnit.GRAM);
        
        // Equal objects must have equal hash codes
        assertTrue(q1.equals(q2));
        assertEquals(q1.hashCode(), q2.hashCode());
    }

    // Equals Contract Tests
    @Test
    void testEquals_GenericQuantity_ContractPreservation() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> q2 = new QuantityClass<>(1000.0, WeightUnit.GRAM);
        QuantityClass<WeightUnit> q3 = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        
        // Reflexive: q1.equals(q1)
        assertTrue(q1.equals(q1));
        
        // Symmetric: q1.equals(q2) implies q2.equals(q1)
        assertTrue(q1.equals(q2));
        assertTrue(q2.equals(q1));
        
        // Transitive: q1.equals(q2) and q2.equals(q3) implies q1.equals(q3)
        assertTrue(q1.equals(q2) && q2.equals(q3) && q1.equals(q3));
    }

    // Enum Behavior Tests
    @Test
    void testEnumAsUnitCarrier_BehaviorEncapsulation() {
        IMeasurable lengthUnit = LengthUnit.FEET;
        IMeasurable weightUnit = WeightUnit.KILOGRAM;
        
        // Polymorphic calls work correctly
        assertEquals(1.0, lengthUnit.getConversionFactor(), EPSILON);
        assertEquals(1.0, weightUnit.getConversionFactor(), EPSILON);
    }

    // Type Erasure Safety
    @Test
    void testTypeErasure_RuntimeSafety() {
        QuantityClass<LengthUnit> length = new QuantityClass<>(1.0, LengthUnit.FEET);
        QuantityClass<WeightUnit> weight = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        
        // Despite type erasure, category checking works
        assertFalse(length.equals(weight));
    }

    // Immutability Tests
    @Test
    void testImmutability_GenericQuantity() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        double originalValue = q1.getValue();
        WeightUnit originalUnit = q1.getUnit();
        
        // Operations return new instances
        QuantityClass<WeightUnit> q2 = q1.convertTo(WeightUnit.GRAM);
        
        // Original unchanged
        assertEquals(originalValue, q1.getValue(), EPSILON);
        assertEquals(originalUnit, q1.getUnit());
        
        // New instance created
        assertNotSame(q1, q2);
    }

    // Null Handling
    @Test
    void testNullHandling_Equality() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        assertFalse(q1.equals(null));
    }

//    @Test
//    void testNullHandling_Addition() {
//        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
//        assertThrows(IllegalArgumentException.class, () -> {
//            QuantityClass.add(q1, null);
//        });
//    }

    // Same Reference Equality
    @Test
    void testSameReference_Equality() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        assertTrue(q1.equals(q1));
    }

    // Zero and Negative Value Tests
    @Test
    void testZeroValue_Equality() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(0.0, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> q2 = new QuantityClass<>(0.0, WeightUnit.GRAM);
        assertTrue(q1.equals(q2));
    }

    @Test
    void testNegativeValue_Equality() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(-1.0, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> q2 = new QuantityClass<>(-1000.0, WeightUnit.GRAM);
        assertTrue(q1.equals(q2));
    }

    // Large Value Tests
    @Test
    void testLargeValue_Equality() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1000000.0, WeightUnit.GRAM);
        QuantityClass<WeightUnit> q2 = new QuantityClass<>(1000.0, WeightUnit.KILOGRAM);
        assertTrue(q1.equals(q2));
    }

    // Round Trip Conversion
    @Test
    void testConversion_RoundTrip() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1.5, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> result = q1.convertTo(WeightUnit.GRAM).convertTo(WeightUnit.KILOGRAM);
        assertEquals(1.5, result.getValue(), EPSILON);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    // Addition Commutativity
    @Test
    void testAddition_Commutativity() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> q2 = new QuantityClass<>(1000.0, WeightUnit.GRAM);
        
        QuantityClass<WeightUnit> result1 = q1.add(q2, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> result2 = q2.add(q1, WeightUnit.KILOGRAM);
        
        assertEquals(result1.getValue(), result2.getValue(), EPSILON);
        assertEquals(result1.getUnit(), result2.getUnit());
    }

    // Addition with Zero
    @Test
    void testAddition_WithZero() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(5.0, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> q2 = new QuantityClass<>(0.0, WeightUnit.GRAM);
        QuantityClass<WeightUnit> result = q1.add(q2);
        assertEquals(5.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    // Addition with Negative Values
    @Test
    void testAddition_NegativeValues() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(5.0, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> q2 = new QuantityClass<>(-2000.0, WeightUnit.GRAM);
        QuantityClass<WeightUnit> result = q1.add(q2);
        assertEquals(3.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    // Addition with Large Values
    @Test
    void testAddition_LargeValues() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1e6, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> q2 = new QuantityClass<>(1e6, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> result = q1.add(q2);
        assertEquals(2e6, result.getValue(), EPSILON);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    // Interface Segregation
    @Test
    void testInterfaceSegregation_MinimalContract() {
        // IMeasurable has exactly 4 methods
        assertEquals(4, IMeasurable.class.getDeclaredMethods().length);
    }

    // toString Tests
    @Test
    void testToString_Format() {
        QuantityClass<WeightUnit> q1 = new QuantityClass<>(1.5, WeightUnit.KILOGRAM);
        String result = q1.toString();
        assertTrue(result.contains("1.5") || result.contains("1.50"));
        assertTrue(result.contains("KILOGRAM"));
    }
}
