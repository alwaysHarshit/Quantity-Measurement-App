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
            double v3= readDouble(reader, "Enter value 1: ");
            double v4 = readDouble(reader, "Enter value 2: ");
            QuantityLength q3=new QuantityLength(v3,LengthUnit.CM);
            QuantityLength q4=new QuantityLength(v4,LengthUnit.YARDS);
            boolean equals = q3.equals(q4);
            System.out.printf("Are lengths both lenghts %.2f in cm and %.2f in yards are equal? %b",v3,v4,equals);

        } catch (IOException e) {
            System.out.println("Input error: " + e.getMessage());
        }
    }
}