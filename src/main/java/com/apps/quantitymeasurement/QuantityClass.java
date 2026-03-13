package com.apps.quantitymeasurement;

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

    public QuantityClass<U> convertTo(U targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double valueInBase = this.unit.convertToBaseUnit(this.value);
        double resultValue = targetUnit.convertFromBaseUnit(valueInBase);
        return new QuantityClass<>(resultValue, targetUnit);
    }

     static <U extends IMeasurable> QuantityClass<U> add(
            QuantityClass<U> quantity1,
            QuantityClass<U> quantity2,
            U targetUnit) {

        if (quantity1 == null || quantity2 == null) {
            throw new IllegalArgumentException("Quantity objects cannot be null");
        }
        if (quantity1.unit == null || quantity2.unit == null) {
            throw new IllegalArgumentException("Units cannot be null");
        }
        if (!Double.isFinite(quantity1.value) || !Double.isFinite(quantity2.value)) {
            throw new IllegalArgumentException("Values must be finite");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double value1InBase = quantity1.unit.convertToBaseUnit(quantity1.value);
        double value2InBase = quantity2.unit.convertToBaseUnit(quantity2.value);
        double sumInBase = value1InBase + value2InBase;
        double resultValue = targetUnit.convertFromBaseUnit(sumInBase);

        return new QuantityClass<>(resultValue, targetUnit);
    }


    public QuantityClass<U> add(QuantityClass<U> other, U targetUnit) {
        return add(this, other, targetUnit);
    }


    public static <U extends IMeasurable> QuantityClass<U> add(
            QuantityClass<U> quantity1,
            QuantityClass<U> quantity2) {

        if (quantity1 == null || quantity2 == null) {
            throw new IllegalArgumentException("Quantity objects cannot be null");
        }

        return add(quantity1, quantity2, quantity1.unit);
    }

    public QuantityClass<U> add(QuantityClass<U> other) {
        return add(this, other);
    }

    /***************************** Subtraction Methods **********************************/

    public static <U extends IMeasurable> QuantityClass<U> subtract(
            QuantityClass<U> q1,
            QuantityClass<U> q2,
            U targetUnit) {

        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException("Quantity objects cannot be null");
        }
        if (q1.unit == null || q2.unit == null) {
            throw new IllegalArgumentException("Units cannot be null");
        }
        if (!Double.isFinite(q1.value) || !Double.isFinite(q2.value)) {
            throw new IllegalArgumentException("Values must be finite");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double value1InBase = q1.unit.convertToBaseUnit(q1.value);
        double value2InBase = q2.unit.convertToBaseUnit(q2.value);
        double differenceInBase = value1InBase - value2InBase;
        double resultValue = targetUnit.convertFromBaseUnit(differenceInBase);

        return new QuantityClass<>(resultValue, targetUnit);
    }

    public QuantityClass<U> subtract(QuantityClass<U> other, U targetUnit) {
        return subtract(this, other, targetUnit);
    }

    public static <U extends IMeasurable> QuantityClass<U> subtract(
            QuantityClass<U> quantity1,
            QuantityClass<U> quantity2) {

        if (quantity1 == null || quantity2 == null) {
            throw new IllegalArgumentException("Quantity objects cannot be null");
        }

        return subtract(quantity1, quantity2, quantity1.unit);
    }


    public QuantityClass<U> subtract(QuantityClass<U> other) {
        return subtract(this, other);
    }

    /***************************** Division Methods **********************************/
    public static <U extends IMeasurable> QuantityClass<U> divide(
            QuantityClass<U> quantity,
            double divisor,
            U targetUnit) {

        if (quantity == null) {
            throw new IllegalArgumentException("Quantity object cannot be null");
        }
        if (quantity.unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (!Double.isFinite(quantity.value)) {
            throw new IllegalArgumentException("Value must be finite");
        }
        if (!Double.isFinite(divisor)) {
            throw new IllegalArgumentException("Divisor must be finite");
        }
        if (Double.compare(divisor, 0.0) == 0) {
            throw new IllegalArgumentException("Divisor cannot be zero");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double valueInBase = quantity.unit.convertToBaseUnit(quantity.value);
        double quotientInBase = valueInBase / divisor;
        double resultValue = (targetUnit.convertFromBaseUnit(quotientInBase)*100)/100;

        return new QuantityClass<>(resultValue, targetUnit);
    }

    public QuantityClass<U> divide(double divisor, U targetUnit) {
        return divide(this, divisor, targetUnit);
    }

    public static <U extends IMeasurable> QuantityClass<U> divide(
            QuantityClass<U> quantity,
            double divisor) {

        if (quantity == null) {
            throw new IllegalArgumentException("Quantity object cannot be null");
        }

        return divide(quantity, divisor, quantity.unit);
    }

    public QuantityClass<U> divide(double divisor) {
        return divide(this, divisor);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", value, unit.getUnitName());
    }
}
