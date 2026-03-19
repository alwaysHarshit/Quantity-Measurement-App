package com.apps.quantitymeasurement.utils;

public enum WeightUnit implements IMeasurable {
    //BASE UNIT IS KG
    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double conversionFactor;

    WeightUnit(double conversionFacotor) {
        this.conversionFactor = conversionFacotor;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value*conversionFactor;
    }

    @Override
    public double convertFromBaseUnit(double value) {
        return value/conversionFactor;
    }

    @Override
    public String getUnitName() {
        return this.name();
    }
}
