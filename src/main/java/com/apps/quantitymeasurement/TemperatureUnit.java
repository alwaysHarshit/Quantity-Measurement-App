package com.apps.quantitymeasurement;

import java.util.function.Function;

public enum TemperatureUnit implements IMeasurable {
    
    // Lambda expressions define conversion TO Celsius (base unit)
    CELSIUS(celsius -> celsius),                    // Identity: C → C
    FAHRENHEIT(fahrenheit -> (fahrenheit - 32) * 5.0 / 9.0);  // F → C
    
    private final Function<Double, Double> toBaseUnit;
    
    // Lambda: Temperature does NOT support arithmetic
    private static final SupportsArithmetic supportsArithmetic = () -> false;
    
    TemperatureUnit(Function<Double, Double> toBaseUnit) {
        this.toBaseUnit = toBaseUnit;
    }
    
    @Override
    public double getConversionFactor() {
        // Temperature doesn't use simple factors
        throw new UnsupportedOperationException(
            "Temperature conversions don't use simple factors. Use convertToBaseUnit() instead.");
    }
    
    @Override
    public double convertToBaseUnit(double value) {
        return toBaseUnit.apply(value);  // Apply the lambda function
    }
    
    @Override
    public double convertFromBaseUnit(double baseValue) {
        // Base unit is Celsius, convert FROM Celsius to this unit
        switch (this) {
            case CELSIUS:
                return baseValue;  // C → C
            case FAHRENHEIT:
                return (baseValue * 9.0 / 5.0) + 32;  // C → F
            default:
                throw new IllegalStateException("Unknown temperature unit");
        }
    }
    
    @Override
    public String getUnitName() {
        return this.name();
    }
    
    @Override
    public boolean supportsArithmetic() {
        return supportsArithmetic.isSupported();  // Returns false
    }
    
    @Override
    public void validateOperationSupport(String operation) {
        if (!supportsArithmetic()) {
            throw new UnsupportedOperationException(
                "Operation " + operation + " is not supported for Temperature units. " +
                "Temperature addition/subtraction/division are not meaningful operations.");
        }
    }
}
