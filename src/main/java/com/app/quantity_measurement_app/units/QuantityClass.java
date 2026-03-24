package com.app.quantity_measurement_app.units;

public class QuantityClass<U extends IMeasurable> {
    private final double value;
    private final U unit;
    
    public QuantityClass(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        QuantityClass<?> other = (QuantityClass<?>) obj;

        if (!this.unit.getClass().equals(other.unit.getClass())) {
            return false;
        }

        return Double.compare(
                this.unit.convertToBaseUnit(this.value),
                other.unit.convertToBaseUnit(other.value)
        ) == 0;
    }

     static <U extends IMeasurable> QuantityClass<U> add(QuantityClass<U> quantity1, QuantityClass<U> quantity2, U targetUnit) {

        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

         double result = performOperation(quantity1, quantity2, ArithmeticOperation.ADD);

         double resultValue = targetUnit.convertFromBaseUnit(result);

        return new QuantityClass<>(resultValue, targetUnit);
    }

     static <U extends IMeasurable> QuantityClass<U> add(QuantityClass<U> quantity1, QuantityClass<U> quantity2) {
        return add(quantity1, quantity2, quantity1.unit);
    }

    public QuantityClass<U> add(QuantityClass<U> other, U targetUnit) {
        return add(this, other, targetUnit);
    }

    public QuantityClass<U> add(QuantityClass<U> other) {
        return add(this, other);
    }

    /***************************** Subtraction Methods **********************************/

     static <U extends IMeasurable> QuantityClass<U> subtract(QuantityClass<U> q1, QuantityClass<U> q2, U targetUnit) {

        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

         double result = performOperation(q1, q2, ArithmeticOperation.SUBTRACT);

         double resultValue = targetUnit.convertFromBaseUnit(result);

        return new QuantityClass<>(resultValue, targetUnit);
    }

    static <U extends IMeasurable> QuantityClass<U> subtract(QuantityClass<U> quantity1, QuantityClass<U> quantity2) {
         return subtract(quantity1, quantity2, quantity1.unit);
    }

    public QuantityClass<U> subtract(QuantityClass<U> other, U targetUnit) {
        return subtract(this, other, targetUnit);
    }

    public QuantityClass<U> subtract(QuantityClass<U> other) {
        return subtract(this, other);
    }

    /***************************** Division Methods **********************************/
     static <U extends IMeasurable> QuantityClass<U> divide(QuantityClass<U> quantity1, QuantityClass<U> quantity2, U targetUnit) {

        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

         double result = performOperation(quantity1, quantity2, ArithmeticOperation.DIVIDE);

         double resultValue = targetUnit.convertFromBaseUnit(result);

        return new QuantityClass<>(resultValue, targetUnit);
    }

    public QuantityClass<U> divide(QuantityClass<U> other, U targetUnit) {
        return divide(this, other, targetUnit);
    }

    public static <U extends IMeasurable> QuantityClass<U> divide(QuantityClass<U> quantity1, QuantityClass<U> quantity2) {
         return divide(quantity1, quantity2, quantity1.unit);
    }

    public QuantityClass<U> divide(QuantityClass<U> other) {
        return divide(this, other);
    }

    public QuantityClass<U> convertTo(U targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double valueInBase = this.unit.convertToBaseUnit(this.value);
        double resultValue = targetUnit.convertFromBaseUnit(valueInBase);
        return new QuantityClass<>(resultValue, targetUnit);
    }

    /***************************** Heler Methods **********************************/


    private static <U extends IMeasurable> double performOperation(
            QuantityClass<U> q1,
            QuantityClass<U> q2,
            ArithmeticOperation operation) {

        //validation
        if (q1 == null || q2 == null)
            throw new IllegalArgumentException("Operands cannot be null");

        if (!q1.unit.getClass().equals(q2.unit.getClass()))
            throw new IllegalArgumentException("Unit categories must match");

        if (!Double.isFinite(q1.value) || !Double.isFinite(q2.value))
            throw new IllegalArgumentException("Operands must be finite");

        q1.unit.validateOperationSupport(operation.name());

        //transform
        double base1 = q1.unit.convertToBaseUnit(q1.value);
        double base2 = q2.unit.convertToBaseUnit(q2.value);

        if (operation == ArithmeticOperation.DIVIDE && base2 == 0)
            throw new ArithmeticException("Division by zero");

        //apply the operation
        return operation.apply(base1, base2);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", value, unit.getUnitName());
    }
}
