package com.apps.quantitymeasurement;


public class QuantityClass<U extends IMeasurable> {
    private final double value;
    private final U unit;

    // Creates one quantity object with a numeric value and a unit.
    // Example: new QuantityClass<>(5, FEET)
    // Validation here ensures that every created object is valid.
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

    // Returns the numeric value part of the quantity.
    public double getValue() {
        return value;
    }

    // Returns the unit part of the quantity.
    public U getUnit() {
        return unit;
    }

    @Override
    // Checks whether two quantities represent the same actual amount.
    // It converts both quantities to their base unit before comparing.
    // Example: 12 INCH can be equal to 1 FOOT.
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

    // Converts this quantity into another unit of the same measurement type.
    // Example: if this is 12 INCH and targetUnit is FOOT, the result is 1 FOOT.
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


     // This is the main add method that contains the real addition logic.
     // All other add methods eventually call this one.
     // Working:
     // 1. Convert quantity1 to base unit
     // 2. Convert quantity2 to base unit
     // 3. Add both base-unit values
     // 4. Convert the sum to targetUnit
     // 5. Return a new QuantityClass object
     // Neither quantity1 nor quantity2 is modified.
     public static <U extends IMeasurable> QuantityClass<U> add( QuantityClass<U> quantity1, QuantityClass<U> quantity2, U targetUnit) {

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


    // Instance version of add with a target unit.
    // When obj1.add(obj2, FOOT) is called:
    // this  -> obj1
    // other -> obj2
    // Then it forwards the call to add(obj1, obj2, FOOT).
    public QuantityClass<U> add(QuantityClass<U> other, U targetUnit) {
        return add(this, other, targetUnit);
    }


    // Static add without target unit.
    // This uses the unit of quantity1 as the result unit.
    // Internally it calls the main add method:
    // add(quantity1, quantity2, quantity1.unit)
    public static <U extends IMeasurable> QuantityClass<U> add(
            QuantityClass<U> quantity1,
            QuantityClass<U> quantity2) {

        if (quantity1 == null || quantity2 == null) {
            throw new IllegalArgumentException("Quantity objects cannot be null");
        }

        // Result in the unit of the first operand
        return add(quantity1, quantity2, quantity1.unit);
    }

    // Most convenient instance add method.
    // When you write obj1.add(obj2), Java uses:
    // add(this, other)
    // which becomes:
    // add(obj1, obj2)
    // which then becomes:
    // add(obj1, obj2, obj1.unit)
    // So the result is returned in obj1's unit.
    public QuantityClass<U> add(QuantityClass<U> other) {
        return add(this, other);
    }

    @Override
    // Returns a readable string version of the quantity.
    // Example: 12.00 Inch
    public String toString() {
        return String.format("%.2f %s", value, unit.getUnitName());
    }
}
