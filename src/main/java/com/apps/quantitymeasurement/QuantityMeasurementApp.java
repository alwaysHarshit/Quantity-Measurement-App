package com.apps.quantitymeasurement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class QuantityMeasurementApp {

    private static double readDouble(BufferedReader reader, String prompt) throws IOException {
        while (true) {
            System.out.print(prompt);
            String input = reader.readLine();

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            double result1 = QuantityLength.convert(
                    1,
                    LengthUnit.FEET,
                    LengthUnit.INCHES
            );

            double result2 = QuantityLength.convert(
                    36,
                    LengthUnit.INCHES,
                    LengthUnit.YARDS
            );

            System.out.println("1 foot in inches: " + result1);
            System.out.println("36 inches in yards: " + result2);

    }
}