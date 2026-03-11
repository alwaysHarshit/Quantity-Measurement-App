package com.apps.quantitymeasurement;

public enum LengthUnit implements IMeasurable {

    //base unit is feet
    FEET(1.0),
    INCHES(0.0833333),
    YARDS(3.0),
    CM(0.0328);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }


    @Override
    public double getConversionFactor() {
        return this.conversionFactor;
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