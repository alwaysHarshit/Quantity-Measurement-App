package com.apps.quantitymeasurement;


public class QuantityMeasurementApp {


    public static void main(String[] args) {
        QuantityWeight qw1=new QuantityWeight(1.0,WeightUnit.KILOGRAM);
        QuantityWeight qw2=new QuantityWeight(2.0,WeightUnit.KILOGRAM);
        System.out.println(qw1.equals(qw2));



    }
}