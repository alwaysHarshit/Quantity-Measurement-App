package com.apps.quantitymeasurement;


public class QuantityClass<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public QuantityClass(double value, U unit) {
        if(unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if(!Double.isFinite(value)) {
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
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        QuantityClass<?> other = (QuantityClass<?>) obj;

        // Ensure units are from the same category (same enum type)
        if (!this.unit.getClass().equals(other.unit.getClass())) {
            return false;
        }

        // Convert both to base unit and compare
        return Double.compare(
                this.unit.convertToBaseUnit(this.value),
                other.unit.convertToBaseUnit(other.value)
        ) == 0;
    }



    public QuantityClass<U> convertTo(U targetUnit) {
        if(targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        // Convert to base unit
        double valueInBase = this.unit.convertToBaseUnit(this.value);

        // Convert from base to target unit
        double resultValue = targetUnit.convertFromBaseUnit(valueInBase);

        return new QuantityClass<>(resultValue, targetUnit);
    }

     static <U extends IMeasurable> QuantityClass<U> add(
            QuantityClass<U> quantity1,
            QuantityClass<U> quantity2,
            U targetUnit) {

        // Validate non-null
        if (quantity1 == null || quantity2 == null) {
            throw new IllegalArgumentException("Quantity objects cannot be null");
        }

        // Validate units
        if (quantity1.unit == null || quantity2.unit == null) {
            throw new IllegalArgumentException("Units cannot be null");
        }

        // Validate finite values
        if (!Double.isFinite(quantity1.value) || !Double.isFinite(quantity2.value)) {
            throw new IllegalArgumentException("Values must be finite");
        }

        // Validate target unit
        if(targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        // Convert both to base unit
        double value1InBase = quantity1.unit.convertToBaseUnit(quantity1.value);
        double value2InBase = quantity2.unit.convertToBaseUnit(quantity2.value);

        // Add the converted values
        double sumInBase = value1InBase + value2InBase;

        // Convert sum to the target unit
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

        // Result in the unit of the first operand
        return add(quantity1, quantity2, quantity1.unit);
    }

    public QuantityClass<U> add(QuantityClass<U> other) {
        return add(this, other);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", value, unit.getUnitName());
    }
}
