package com.apps.quantitymeasurement;


enum LengthUnit {

    FEET(12.0),
    INCHES(1.0),
    YARDS(36.0),
    CM(0.393701);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }
}

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
                this.convertToBaseUnit(),
                other.convertToBaseUnit()
        ) == 0;
    }

    private double convertToBaseUnit() {
        return value * unit.getConversionFactor();

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
        double valueInBase = value * sourceUnit.getConversionFactor();

        //Convert base → target
        double result = valueInBase / targetUnit.getConversionFactor();

        return Math.round(result * 100.0) / 100.0;
    }

    public static QuantityLength add(QuantityLength length1, QuantityLength length2) {
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

        // Convert both to base unit (inches)
        double value1InBase = length1.convertToBaseUnit();
        double value2InBase = length2.convertToBaseUnit();

        // Add the converted values
        double sumInBase = value1InBase + value2InBase;

        // Convert sum to the unit of the first operand
        double resultValue = sumInBase / length1.unit.getConversionFactor();

        return new QuantityLength(resultValue, length1.unit);
    }

    public QuantityLength add(QuantityLength other) {
        return add(this, other);
    }

}
