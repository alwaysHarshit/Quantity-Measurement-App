package com.apps.quantitymeasurement;

public enum LengthUnit {

    //base unit is feet
    FEET(1.0),
    INCHES(0.0833333),
    YARDS(3.0),
    CM(0.0328);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }


    public double convertToBaseUnit(double value) {
        return value*conversionFactor;
    }

    public double convertFromBaseUnit(double value) {
        return value/conversionFactor;
    }
}