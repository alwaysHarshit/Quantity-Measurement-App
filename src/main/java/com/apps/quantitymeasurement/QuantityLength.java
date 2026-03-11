package com.apps.quantitymeasurement;

public class QuantityLength {

    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        QuantityLength other = (QuantityLength) obj;
        return Double.compare(
                this.unit.convertToBaseUnit(value),
                other.unit.convertToBaseUnit(value)
        ) == 0;
    }


    @Override
    public String toString() {
        return String.format("%.2f %s", value,unit);
    }

    public static double convert(double value, LengthUnit sourceUnit, LengthUnit targetUnit) {

        //Validate value
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }

        //Validate units
        if (sourceUnit == null || targetUnit == null) {
            throw new IllegalArgumentException("Units cannot be null");
        }

        // Convert source → base unit
        double valueInBase = sourceUnit.convertToBaseUnit(value);

        //Convert base → target
        double result = targetUnit.convertFromBaseUnit(valueInBase);

        return Math.round(result * 100.0) / 100.0;
    }

    public static QuantityLength add(QuantityLength length1, QuantityLength length2,LengthUnit targetUnit) {
        // Validate non-null
        if (length1 == null || length2 == null) {
            throw new IllegalArgumentException("Length objects cannot be null");
        }

        // Validate units
        if (length1.unit == null || length2.unit == null) {
            throw new IllegalArgumentException("Units cannot be null");
        }

        // Validate finite values
        if (!Double.isFinite(length1.value) || !Double.isFinite(length2.value)) {
            throw new IllegalArgumentException("Values must be finite");
        }
        //validating target unit
        if(targetUnit == null ) throw new IllegalArgumentException("Units cannot be null");

        // Convert both to base unit (inches)
        double value1InBase = length1.unit.convertToBaseUnit(length1.value);
        double value2InBase = length2.unit.convertToBaseUnit(length2.value);

        System.out.printf("value1 %f in inch, value2 %f in inch",value1InBase,value2InBase);

        // Add the converted values
        double sumInBase = value1InBase + value2InBase;

        // Convert sum to the unit of the first operand
        double resultValue = targetUnit.convertFromBaseUnit(sumInBase);
        System.out.printf("result value in %s unit is %f",LengthUnit.valueOf(String.valueOf(targetUnit)),resultValue);

        return new QuantityLength(resultValue,targetUnit);
    }

    public QuantityLength add(QuantityLength other,LengthUnit target) {
        return add(this, other,target);
    }
    public static QuantityLength add(QuantityLength length1, QuantityLength length2) {

        if (length1 == null || length2 == null)
            throw new IllegalArgumentException("Length objects cannot be null");

        // result should be returned in the unit of the first operand
        return add(length1, length2, length1.unit);
    }

}
