package com.app.quantity_measurement_app.units;

public interface IMeasurable {
    double getConversionFactor();
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
    String getUnitName();

    @FunctionalInterface
    interface SupportsArithmetic {
        boolean isSupported();
    }
    SupportsArithmetic DEFAULT_ARITHMETIC_SUPPORT = () -> true;

    //these default methods are inherited by enums
    default boolean supportsArithmetic() { return DEFAULT_ARITHMETIC_SUPPORT.isSupported(); }
    default void validateOperationSupport(String operation) {}

}
