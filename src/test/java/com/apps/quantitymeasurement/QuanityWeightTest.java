package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class QuanityWeightTest {
    private static final double EPSILON = 1e-6;

    @Test
    void  testEquality_KilogramToKilogram_SameValue(){
        QuantityWeight qw1=new QuantityWeight(1.0,WeightUnit.KILOGRAM);
        QuantityWeight qw2=new QuantityWeight(1.0,WeightUnit.KILOGRAM);
        assertTrue(qw1.equals(qw2));
    }
    @Test
    void testEquality_KilogramToKilogram_DifferentValue(){
        QuantityWeight qw1=new QuantityWeight(2.0,WeightUnit.KILOGRAM);
        QuantityWeight qw2=new QuantityWeight(1.0,WeightUnit.KILOGRAM);
        assertFalse(qw1.equals(qw2));
    }
    @Test
    void testEquality_KilogramToGram_EquivalentValue(){
        QuantityWeight qw1=new QuantityWeight(1.0,WeightUnit.KILOGRAM);
        QuantityWeight qw2=new QuantityWeight(1000.0,WeightUnit.GRAM);
        assertTrue(qw1.equals(qw2));
    }

    @Test
    void testEquality_GramToKilogram_EquivalentValue(){
        QuantityWeight qw1=new QuantityWeight(1.0,WeightUnit.KILOGRAM);
        QuantityWeight qw2=new QuantityWeight(1000.0,WeightUnit.GRAM);
        assertTrue(qw2.equals(qw1));
    }

    @Test
    void testEquality_GramToGram_SameValue(){
        QuantityWeight qw1=new QuantityWeight(100.0,WeightUnit.GRAM);
        QuantityWeight qw2=new QuantityWeight(100.0,WeightUnit.GRAM);
        assertTrue(qw1.equals(qw2));
    }

    @Test
    void testEquality_PoundToPound_SameValue(){
        QuantityWeight qw1=new QuantityWeight(5.0,WeightUnit.POUND);
        QuantityWeight qw2=new QuantityWeight(5.0,WeightUnit.POUND);
        assertTrue(qw1.equals(qw2));
    }

    @Test
    void testEquality_KilogramToPound_EquivalentValue(){
        QuantityWeight qw1=new QuantityWeight(1.0,WeightUnit.KILOGRAM);
        QuantityWeight qw2=new QuantityWeight(2.20462,WeightUnit.POUND);
        assertTrue(qw1.equals(qw2));
    }

    @Test
    void testEquality_GramToPound_EquivalentValue(){
        QuantityWeight qw1=new QuantityWeight(453.592,WeightUnit.GRAM);
        QuantityWeight qw2=new QuantityWeight(1.0,WeightUnit.POUND);
        assertTrue(qw1.equals(qw2));
    }


    @Test
    void testEquality_NullComparison(){
        QuantityWeight qw=new QuantityWeight(1.0,WeightUnit.KILOGRAM);
        assertFalse(qw.equals(null));
    }

    @Test
    void testEquality_SameReference(){
        QuantityWeight qw=new QuantityWeight(1.0,WeightUnit.KILOGRAM);
        assertTrue(qw.equals(qw));
    }

    @Test
    void testEquality_NullUnit(){
        assertThrows(IllegalArgumentException.class, () -> {
            new QuantityWeight(1.0,null);
        });
    }

    @Test
    void testEquality_TransitiveProperty(){
        QuantityWeight qw1=new QuantityWeight(1.0,WeightUnit.KILOGRAM);
        QuantityWeight qw2=new QuantityWeight(1000.0,WeightUnit.GRAM);
        QuantityWeight qw3=new QuantityWeight(1.0,WeightUnit.KILOGRAM);
        assertTrue(qw1.equals(qw2) && qw2.equals(qw3) && qw1.equals(qw3));
    }

    @Test
    void testEquality_ZeroValue(){
        QuantityWeight qw1=new QuantityWeight(0.0,WeightUnit.KILOGRAM);
        QuantityWeight qw2=new QuantityWeight(0.0,WeightUnit.GRAM);
        assertTrue(qw1.equals(qw2));
    }

    @Test
    void testEquality_NegativeWeight(){
        QuantityWeight qw1=new QuantityWeight(-1.0,WeightUnit.KILOGRAM);
        QuantityWeight qw2=new QuantityWeight(-1000.0,WeightUnit.GRAM);
        assertTrue(qw1.equals(qw2));
    }

    @Test
    void testEquality_LargeWeightValue(){
        QuantityWeight qw1=new QuantityWeight(1000000.0,WeightUnit.GRAM);
        QuantityWeight qw2=new QuantityWeight(1000.0,WeightUnit.KILOGRAM);
        assertTrue(qw1.equals(qw2));
    }

    @Test
    void testEquality_SmallWeightValue(){
        QuantityWeight qw1=new QuantityWeight(0.001,WeightUnit.KILOGRAM);
        QuantityWeight qw2=new QuantityWeight(1.0,WeightUnit.GRAM);
        assertTrue(qw1.equals(qw2));
    }

    @Test
    void testConversion_PoundToKilogram(){
        QuantityWeight qw1=new QuantityWeight(2.20462,WeightUnit.POUND);
        QuantityWeight result=qw1.convertTo(WeightUnit.KILOGRAM);
        assertEquals(1.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    @Test
    void testConversion_KilogramToPound(){
        QuantityWeight qw1=new QuantityWeight(1.0,WeightUnit.KILOGRAM);
        QuantityWeight result=qw1.convertTo(WeightUnit.POUND);
        assertEquals(2.20462, result.getValue(), EPSILON);
        assertEquals(WeightUnit.POUND, result.getUnit());
    }

    @Test
    void testConversion_SameUnit(){
        QuantityWeight qw1=new QuantityWeight(5.0,WeightUnit.KILOGRAM);
        QuantityWeight result=qw1.convertTo(WeightUnit.KILOGRAM);
        assertEquals(5.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    @Test
    void testConversion_ZeroValue(){
        QuantityWeight qw1=new QuantityWeight(0.0,WeightUnit.KILOGRAM);
        QuantityWeight result=qw1.convertTo(WeightUnit.GRAM);
        assertEquals(0.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.GRAM, result.getUnit());
    }

    @Test
    void testConversion_NegativeValue(){
        QuantityWeight qw1=new QuantityWeight(-1.0,WeightUnit.KILOGRAM);
        QuantityWeight result=qw1.convertTo(WeightUnit.GRAM);
        assertEquals(-1000.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.GRAM, result.getUnit());
    }

    @Test
    void testConversion_RoundTrip(){
        QuantityWeight qw1=new QuantityWeight(1.5,WeightUnit.KILOGRAM);
        QuantityWeight result=qw1.convertTo(WeightUnit.GRAM).convertTo(WeightUnit.KILOGRAM);
        assertEquals(1.5, result.getValue(), EPSILON);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    @Test
    void testAddition_SameUnit_KilogramPlusKilogram(){
        QuantityWeight qw1=new QuantityWeight(1.0,WeightUnit.KILOGRAM);
        QuantityWeight qw2=new QuantityWeight(2.0,WeightUnit.KILOGRAM);
        QuantityWeight result=qw1.add(qw2);
        assertEquals(3.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    @Test
    void testAddition_CrossUnit_KilogramPlusGram(){
        QuantityWeight qw1=new QuantityWeight(1.0,WeightUnit.KILOGRAM);
        QuantityWeight qw2=new QuantityWeight(1000.0,WeightUnit.GRAM);
        QuantityWeight result=qw1.add(qw2);
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    @Test
    void testAddition_CrossUnit_PoundPlusKilogram(){
        QuantityWeight qw1=new QuantityWeight(2.20462,WeightUnit.POUND);
        QuantityWeight qw2=new QuantityWeight(1.0,WeightUnit.KILOGRAM);
        QuantityWeight result=qw1.add(qw2);
        assertEquals(4.40924, result.getValue(), EPSILON);
        assertEquals(WeightUnit.POUND, result.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Kilogram(){
        QuantityWeight qw1=new QuantityWeight(1.0,WeightUnit.KILOGRAM);
        QuantityWeight qw2=new QuantityWeight(1000.0,WeightUnit.GRAM);
        QuantityWeight result=qw1.add(qw2, WeightUnit.GRAM);
        assertEquals(2000.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.GRAM, result.getUnit());
    }

    @Test
    void testAddition_Commutativity(){
        QuantityWeight qw1=new QuantityWeight(1.0,WeightUnit.KILOGRAM);
        QuantityWeight qw2=new QuantityWeight(1000.0,WeightUnit.GRAM);
        QuantityWeight result1=qw1.add(qw2, WeightUnit.KILOGRAM);
        QuantityWeight result2=qw2.add(qw1, WeightUnit.KILOGRAM);
        assertEquals(result1.getValue(), result2.getValue(), EPSILON);
        assertEquals(result1.getUnit(), result2.getUnit());
    }

    @Test
    void testAddition_WithZero(){
        QuantityWeight qw1=new QuantityWeight(5.0,WeightUnit.KILOGRAM);
        QuantityWeight qw2=new QuantityWeight(0.0,WeightUnit.GRAM);
        QuantityWeight result=qw1.add(qw2);
        assertEquals(5.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    @Test
    void testAddition_NegativeValues(){
        QuantityWeight qw1=new QuantityWeight(5.0,WeightUnit.KILOGRAM);
        QuantityWeight qw2=new QuantityWeight(-2000.0,WeightUnit.GRAM);
        QuantityWeight result=qw1.add(qw2);
        assertEquals(3.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    @Test
    void testAddition_LargeValues(){
        QuantityWeight qw1=new QuantityWeight(1e6,WeightUnit.KILOGRAM);
        QuantityWeight qw2=new QuantityWeight(1e6,WeightUnit.KILOGRAM);
        QuantityWeight result=qw1.add(qw2);
        assertEquals(2e6, result.getValue(), EPSILON);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }


}
