package com.apps.quantitymeasurement;

public class QuantityWeight {
    private final double value;
    private final WeightUnit unit;

    public QuantityWeight(double value, WeightUnit unit) {
        if(unit==null) throw new IllegalArgumentException("Unit cannot be null");
        if(!Double.isFinite(value)) throw new IllegalArgumentException("Value must be finite");
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public WeightUnit getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if(obj==null || this.getClass() != obj.getClass()) return false;
        
        QuantityWeight other = (QuantityWeight) obj;
        return Double.compare(
                this.unit.convertToBaseUnit(this.value),
                other.unit.convertToBaseUnit(other.value))==0;
    }

    public QuantityWeight convertTo(WeightUnit targetUnit) {
        if(targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        
        // Convert to base unit (kilogram)
        double valueInBase = this.unit.convertToBaseUnit(this.value);
        
        // Convert from base to target unit
        double resultValue = targetUnit.convertFromBaseUnit(valueInBase);
        
        return new QuantityWeight(resultValue, targetUnit);
    }

    public static QuantityWeight add(QuantityWeight weight1, QuantityWeight weight2, WeightUnit targetUnit) {
        // Validate non-null
        if (weight1 == null || weight2 == null) {
            throw new IllegalArgumentException("Weight objects cannot be null");
        }

        // Validate units
        if (weight1.unit == null || weight2.unit == null) {
            throw new IllegalArgumentException("Units cannot be null");
        }

        // Validate finite values
        if (!Double.isFinite(weight1.value) || !Double.isFinite(weight2.value)) {
            throw new IllegalArgumentException("Values must be finite");
        }
        
        // Validate target unit
        if(targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        // Convert both to base unit (kilogram)
        double value1InBase = weight1.unit.convertToBaseUnit(weight1.value);
        double value2InBase = weight2.unit.convertToBaseUnit(weight2.value);

        // Add the converted values
        double sumInBase = value1InBase + value2InBase;

        // Convert sum to the target unit
        double resultValue = targetUnit.convertFromBaseUnit(sumInBase);

        return new QuantityWeight(resultValue, targetUnit);
    }

    public QuantityWeight add(QuantityWeight other, WeightUnit targetUnit) {
        return add(this, other, targetUnit);
    }

    public static QuantityWeight add(QuantityWeight weight1, QuantityWeight weight2) {
        if (weight1 == null || weight2 == null) {
            throw new IllegalArgumentException("Weight objects cannot be null");
        }

        // Result should be returned in the unit of the first operand
        return add(weight1, weight2, weight1.unit);
    }

    public QuantityWeight add(QuantityWeight other) {
        return add(this, other);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", value, unit);
    }
}
