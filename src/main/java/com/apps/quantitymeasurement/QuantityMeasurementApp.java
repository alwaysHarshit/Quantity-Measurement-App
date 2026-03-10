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

        try {

            double value1 = readDouble(reader, "Enter value 1: ");
            double value2 = readDouble(reader, "Enter value 2: ");

            QuantityLength q1 = new QuantityLength(value1, LengthUnit.INCHES);
            QuantityLength q2 = new QuantityLength(value2, LengthUnit.FEET);

            boolean result = q1.equals(q2);

            System.out.println("Are the two measurements equal? " + result);

        } catch (IOException e) {
            System.out.println("Input error: " + e.getMessage());
        }
    }
}