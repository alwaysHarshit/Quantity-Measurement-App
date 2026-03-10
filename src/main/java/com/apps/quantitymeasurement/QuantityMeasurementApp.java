package com.apps.quantitymeasurement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class QuantityMeasurementApp {


    public static void main(String[] args) {
        QuantityLength add = QuantityLength.add(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(2.0, LengthUnit.FEET));
        System.out.println(add);


    }
}