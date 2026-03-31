package com.app.quantity_measurement_app.units;

public enum ArithmeticOperation {

    ADD{
        @Override
        double apply(double value1, double value2) {
            return value1+value2;
        }
    },
    SUBTRACT{
        @Override
        double apply(double value1, double value2) {
            return value1-value2;
        }
    },
    DIVIDE{
        @Override
        double apply(double value1, double value2) {
            return value1/value2;
        }
    };

    abstract double apply(double value1, double value2);
}
