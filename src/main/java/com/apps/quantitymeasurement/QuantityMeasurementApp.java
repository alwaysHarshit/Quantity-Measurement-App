package com.apps.quantitymeasurement;


public class QuantityMeasurementApp {


    public static void main(String[] args) {
        QuantityLength quantityLength1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength quantityLength2 = new QuantityLength(2.0, LengthUnit.FEET);

        QuantityLength add = QuantityLength.add(quantityLength1, quantityLength2, LengthUnit.CM);
        System.out.println(add);


    }
}