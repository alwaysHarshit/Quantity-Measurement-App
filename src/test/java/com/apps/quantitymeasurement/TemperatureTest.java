package com.apps.quantitymeasurement;

import com.apps.quantitymeasurement.utils.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TemperatureTest {

    // ==================== Equality Tests ====================

    @Test
    void testTemperatureEquality_CelsiusToCelsius_SameValue() {
        QuantityClass<TemperatureUnit> temp1 = new QuantityClass<>(0.0, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> temp2 = new QuantityClass<>(0.0, TemperatureUnit.CELSIUS);
        assertTrue(temp1.equals(temp2));
    }

    @Test
    void testTemperatureEquality_FahrenheitToFahrenheit_SameValue() {
        QuantityClass<TemperatureUnit> temp1 = new QuantityClass<>(32.0, TemperatureUnit.FAHRENHEIT);
        QuantityClass<TemperatureUnit> temp2 = new QuantityClass<>(32.0, TemperatureUnit.FAHRENHEIT);
        assertTrue(temp1.equals(temp2));
    }

    @Test
    void testTemperatureEquality_CelsiusToFahrenheit_0Celsius32Fahrenheit() {
        QuantityClass<TemperatureUnit> celsius = new QuantityClass<>(0.0, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> fahrenheit = new QuantityClass<>(32.0, TemperatureUnit.FAHRENHEIT);
        assertTrue(celsius.equals(fahrenheit));
    }

    @Test
    void testTemperatureEquality_CelsiusToFahrenheit_100Celsius212Fahrenheit() {
        QuantityClass<TemperatureUnit> celsius = new QuantityClass<>(100.0, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> fahrenheit = new QuantityClass<>(212.0, TemperatureUnit.FAHRENHEIT);
        assertTrue(celsius.equals(fahrenheit));
    }

    @Test
    void testTemperatureEquality_CelsiusToFahrenheit_Negative40Equal() {
        QuantityClass<TemperatureUnit> celsius = new QuantityClass<>(-40.0, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> fahrenheit = new QuantityClass<>(-40.0, TemperatureUnit.FAHRENHEIT);
        assertTrue(celsius.equals(fahrenheit));
    }

    @Test
    void testTemperatureEquality_SymmetricProperty() {
        QuantityClass<TemperatureUnit> tempA = new QuantityClass<>(25.0, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> tempB = new QuantityClass<>(77.0, TemperatureUnit.FAHRENHEIT);
        assertTrue(tempA.equals(tempB) && tempB.equals(tempA));
    }

    @Test
    void testTemperatureEquality_ReflexiveProperty() {
        QuantityClass<TemperatureUnit> temp = new QuantityClass<>(50.0, TemperatureUnit.CELSIUS);
        assertTrue(temp.equals(temp));
    }

    @Test
    void testTemperatureDifferentValuesInequality() {
        QuantityClass<TemperatureUnit> temp1 = new QuantityClass<>(50.0, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> temp2 = new QuantityClass<>(100.0, TemperatureUnit.CELSIUS);
        assertFalse(temp1.equals(temp2));
    }

    // ==================== Conversion Tests ====================

    @Test
    void testTemperatureConversion_CelsiusToFahrenheit_VariousValues() {
        // 50°C → 122°F
        QuantityClass<TemperatureUnit> celsius50 = new QuantityClass<>(50.0, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> result = celsius50.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(122.0, result.getValue(), 0.01);

        // -20°C → -4°F
        QuantityClass<TemperatureUnit> celsiusMinus20 = new QuantityClass<>(-20.0, TemperatureUnit.CELSIUS);
        result = celsiusMinus20.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(-4.0, result.getValue(), 0.01);

        // 37°C → 98.6°F (body temperature)
        QuantityClass<TemperatureUnit> celsius37 = new QuantityClass<>(37.0, TemperatureUnit.CELSIUS);
        result = celsius37.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(98.6, result.getValue(), 0.01);
    }

    @Test
    void testTemperatureConversion_FahrenheitToCelsius_VariousValues() {
        // 122°F → 50°C
        QuantityClass<TemperatureUnit> fahrenheit122 = new QuantityClass<>(122.0, TemperatureUnit.FAHRENHEIT);
        QuantityClass<TemperatureUnit> result = fahrenheit122.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(50.0, result.getValue(), 0.01);

        // -4°F → -20°C
        QuantityClass<TemperatureUnit> fahrenheitMinus4 = new QuantityClass<>(-4.0, TemperatureUnit.FAHRENHEIT);
        result = fahrenheitMinus4.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(-20.0, result.getValue(), 0.01);

        // 98.6°F → 37°C
        QuantityClass<TemperatureUnit> fahrenheit986 = new QuantityClass<>(98.6, TemperatureUnit.FAHRENHEIT);
        result = fahrenheit986.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(37.0, result.getValue(), 0.01);
    }

    @Test
    void testTemperatureConversion_RoundTrip_PreservesValue() {
        QuantityClass<TemperatureUnit> original = new QuantityClass<>(75.0, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> converted = original.convertTo(TemperatureUnit.FAHRENHEIT);
        QuantityClass<TemperatureUnit> backToOriginal = converted.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(original.getValue(), backToOriginal.getValue(), 0.0001);
    }

    @Test
    void testTemperatureConversion_SameUnit() {
        QuantityClass<TemperatureUnit> celsius = new QuantityClass<>(100.0, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> result = celsius.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(100.0, result.getValue(), 0.01);
    }

    @Test
    void testTemperatureConversion_ZeroValue() {
        QuantityClass<TemperatureUnit> celsiusZero = new QuantityClass<>(0.0, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> result = celsiusZero.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(32.0, result.getValue(), 0.01);
    }

    @Test
    void testTemperatureConversion_NegativeValues() {
        QuantityClass<TemperatureUnit> celsiusMinus273 = new QuantityClass<>(-273.15, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> result = celsiusMinus273.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(-459.67, result.getValue(), 0.01);
    }

    @Test
    void testTemperatureConversion_LargeValues() {
        QuantityClass<TemperatureUnit> celsius1000 = new QuantityClass<>(1000.0, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> result = celsius1000.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(1832.0, result.getValue(), 0.01);
    }

    @Test
    void testTemperatureConversionPrecision_Epsilon() {
        QuantityClass<TemperatureUnit> celsius = new QuantityClass<>(25.5555, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> fahrenheit = celsius.convertTo(TemperatureUnit.FAHRENHEIT);
        QuantityClass<TemperatureUnit> backToCelsius = fahrenheit.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(celsius.getValue(), backToCelsius.getValue(), 0.0001);
    }

    @Test
    void testTemperatureConversionEdgeCase_VerySmallDifference() {
        QuantityClass<TemperatureUnit> temp1 = new QuantityClass<>(25.0000, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> temp2 = new QuantityClass<>(25.0001, TemperatureUnit.CELSIUS);
        assertFalse(temp1.equals(temp2));
    }

    // ==================== Unsupported Operation Tests ====================

    @Test
    void testTemperatureUnsupportedOperation_Add() {
        QuantityClass<TemperatureUnit> temp1 = new QuantityClass<>(100.0, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> temp2 = new QuantityClass<>(50.0, TemperatureUnit.CELSIUS);

        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> temp1.add(temp2)
        );

        assertTrue(exception.getMessage().contains("ADD"));
        assertTrue(exception.getMessage().contains("not supported"));
    }

    @Test
    void testTemperatureUnsupportedOperation_Subtract() {
        QuantityClass<TemperatureUnit> temp1 = new QuantityClass<>(100.0, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> temp2 = new QuantityClass<>(50.0, TemperatureUnit.CELSIUS);

        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> temp1.subtract(temp2)
        );

        assertTrue(exception.getMessage().contains("SUBTRACT"));
        assertTrue(exception.getMessage().contains("not supported"));
    }

    @Test
    void testTemperatureUnsupportedOperation_Divide() {
        QuantityClass<TemperatureUnit> temp1 = new QuantityClass<>(100.0, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> temp2 = new QuantityClass<>(50.0, TemperatureUnit.CELSIUS);

        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> temp1.divide(temp2)
        );

        assertTrue(exception.getMessage().contains("DIVIDE"));
        assertTrue(exception.getMessage().contains("not supported"));
    }

    @Test
    void testTemperatureUnsupportedOperation_ErrorMessage() {
        QuantityClass<TemperatureUnit> temp1 = new QuantityClass<>(100.0, TemperatureUnit.FAHRENHEIT);
        QuantityClass<TemperatureUnit> temp2 = new QuantityClass<>(50.0, TemperatureUnit.FAHRENHEIT);

        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> temp1.add(temp2)
        );

        assertTrue(exception.getMessage().toLowerCase().contains("temperature"));
    }

    // ==================== Cross-Category Incompatibility Tests ====================

    @Test
    void testTemperatureVsLengthIncompatibility() {
        QuantityClass<TemperatureUnit> temperature = new QuantityClass<>(100.0, TemperatureUnit.CELSIUS);
        QuantityClass<LengthUnit> length = new QuantityClass<>(100.0, LengthUnit.FEET);
        assertFalse(temperature.equals(length));
    }

    @Test
    void testTemperatureVsWeightIncompatibility() {
        QuantityClass<TemperatureUnit> temperature = new QuantityClass<>(50.0, TemperatureUnit.CELSIUS);
        QuantityClass<WeightUnit> weight = new QuantityClass<>(50.0, WeightUnit.KILOGRAM);
        assertFalse(temperature.equals(weight));
    }

    @Test
    void testTemperatureVsVolumeIncompatibility() {
        QuantityClass<TemperatureUnit> temperature = new QuantityClass<>(25.0, TemperatureUnit.CELSIUS);
        QuantityClass<VolumeUnit> volume = new QuantityClass<>(25.0, VolumeUnit.LITRE);
        assertFalse(temperature.equals(volume));
    }

    // ==================== Operation Support Methods Tests ====================

    @Test
    void testOperationSupportMethods_TemperatureUnitAddition() {
        assertFalse(TemperatureUnit.CELSIUS.supportsArithmetic());
    }

    @Test
    void testOperationSupportMethods_TemperatureUnitDivision() {
        assertFalse(TemperatureUnit.FAHRENHEIT.supportsArithmetic());
    }

    @Test
    void testOperationSupportMethods_LengthUnitAddition() {
        assertTrue(LengthUnit.FEET.supportsArithmetic());
    }

    @Test
    void testOperationSupportMethods_WeightUnitDivision() {
        assertTrue(WeightUnit.KILOGRAM.supportsArithmetic());
    }

    @Test
    void testTemperatureValidateOperationSupport_MethodBehavior() {
        assertThrows(
            UnsupportedOperationException.class,
            () -> TemperatureUnit.CELSIUS.validateOperationSupport("ADD")
        );
    }

    // ==================== Interface Evolution & Backward Compatibility ====================

    @Test
    void testIMeasurableInterface_Evolution_BackwardCompatible() {
        // Verify existing units still work without modification
        QuantityClass<LengthUnit> length1 = new QuantityClass<>(12.0, LengthUnit.INCHES);
        QuantityClass<LengthUnit> length2 = new QuantityClass<>(1.0, LengthUnit.FEET);
        QuantityClass<LengthUnit> result = length1.add(length2);
        // 12 inches + 1 foot = 24 inches (result is in first operand's unit)
        assertEquals(24.0, result.getValue(), 0.01);
    }

    @Test
    void testTemperatureDefaultMethodInheritance() {
        // Non-temperature units inherit default true for arithmetic support
        assertTrue(LengthUnit.FEET.supportsArithmetic());
        assertTrue(WeightUnit.KILOGRAM.supportsArithmetic());
        assertTrue(VolumeUnit.LITRE.supportsArithmetic());

        // Temperature overrides to false
        assertFalse(TemperatureUnit.CELSIUS.supportsArithmetic());
        assertFalse(TemperatureUnit.FAHRENHEIT.supportsArithmetic());
    }

    // ==================== TemperatureUnit Enum Tests ====================

    @Test
    void testTemperatureUnit_NonLinearConversion() {
        // Verify temperature uses formulas, not simple multiplication
        double celsius = 100.0;
        double fahrenheitExpected = (celsius * 9.0 / 5.0) + 32.0;

        QuantityClass<TemperatureUnit> temp = new QuantityClass<>(celsius, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> converted = temp.convertTo(TemperatureUnit.FAHRENHEIT);

        assertEquals(fahrenheitExpected, converted.getValue(), 0.01);
    }

    @Test
    void testTemperatureUnit_AllConstants() {
        assertNotNull(TemperatureUnit.CELSIUS);
        assertNotNull(TemperatureUnit.FAHRENHEIT);
    }

    @Test
    void testTemperatureUnit_NameMethod() {
        assertEquals("CELSIUS", TemperatureUnit.CELSIUS.getUnitName());
        assertEquals("FAHRENHEIT", TemperatureUnit.FAHRENHEIT.getUnitName());
    }

    @Test
    void testTemperatureUnit_ConversionFactor() {
        // getConversionFactor() should throw exception for Temperature
        assertThrows(
            UnsupportedOperationException.class,
            () -> TemperatureUnit.CELSIUS.getConversionFactor()
        );
    }

    // ==================== Validation Tests ====================

    @Test
    void testTemperatureNullUnitValidation() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new QuantityClass<>(100.0, null)
        );
    }

    @Test
    void testTemperatureNullOperandValidation_InComparison() {
        QuantityClass<TemperatureUnit> temp = new QuantityClass<>(100.0, TemperatureUnit.CELSIUS);
        assertFalse(temp.equals(null));
    }

    @Test
    void testTemperatureCrossUnitAdditionAttempt() {
        QuantityClass<TemperatureUnit> celsius = new QuantityClass<>(100.0, TemperatureUnit.CELSIUS);
        QuantityClass<TemperatureUnit> fahrenheit = new QuantityClass<>(100.0, TemperatureUnit.FAHRENHEIT);

        // Both should throw UnsupportedOperationException
        assertThrows(
            UnsupportedOperationException.class,
            () -> celsius.add(fahrenheit)
        );
    }

    // ==================== Integration Tests ====================

    @Test
    void testTemperatureIntegrationWithGenericQuantity() {
        // Verify Quantity<TemperatureUnit> works seamlessly with generic class
        QuantityClass<TemperatureUnit> temp = new QuantityClass<>(25.0, TemperatureUnit.CELSIUS);

        assertNotNull(temp);
        assertEquals(25.0, temp.getValue());
        assertEquals(TemperatureUnit.CELSIUS, temp.getUnit());
        assertEquals("25.00 CELSIUS", temp.toString());
    }

    @Test
    void testTemperatureBackwardCompatibility_ExistingTests() {
        // Run sample existing test to ensure temperature doesn't break other categories
        QuantityClass<LengthUnit> feet = new QuantityClass<>(3.0, LengthUnit.FEET);
        QuantityClass<LengthUnit> inches = new QuantityClass<>(36.0, LengthUnit.INCHES);
        assertTrue(feet.equals(inches));

        QuantityClass<WeightUnit> kg = new QuantityClass<>(1.0, WeightUnit.KILOGRAM);
        QuantityClass<WeightUnit> grams = new QuantityClass<>(1000.0, WeightUnit.GRAM);
        assertTrue(kg.equals(grams));
    }

    @Test
    void testTemperatureEnumImplementsIMeasurable() {
        assertTrue(IMeasurable.class.isAssignableFrom(TemperatureUnit.class));
    }
}
