package com.apps.quantitymeasurement;

public enum WeightUnit {
    //BASE UNIT IS KG
    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double conversionFactor;

    WeightUnit(double conversionFacotor) {
        this.conversionFactor = conversionFacotor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public double convertToBaseUnit(double value) {
        return value*conversionFactor;
    }

    public double convertFromBaseUnit(double value) {
        return value/conversionFactor;
    }
}
