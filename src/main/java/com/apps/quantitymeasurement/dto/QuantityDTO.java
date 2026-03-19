package com.apps.quantitymeasurement.dto;

interface IMeasureableUnit {

    String getUnitName();
    String getUnitType();

}
public final class QuantityDTO {

    private final double value;
    private final String unit;
    private final String measurmentType;

    public QuantityDTO(double value, IMeasureableUnit unit) {
        this.value = value;
        this.unit = unit.getUnitName();
        this.measurmentType = unit.getUnitType();
    }

    // This overload keeps the service free to build DTOs from domain enums or persisted values.
    public QuantityDTO(double value, String unit, String measurmentType) {
        this.value = value;
        this.unit = unit;
        this.measurmentType = measurmentType;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public String getMeasurmentType() {
        return measurmentType;
    }

    public String getMeasurementType() {
        return measurmentType;
    }

    public enum LengthUnit implements IMeasureableUnit {
        FEET, METERS, CENTIMETERS, INCHES, YARDS;

        @Override
        public String getUnitName() {
            return this.name();
        }

        @Override
        public String getUnitType() {
            return this.getClass().getSimpleName();
        }
    }

    public enum WeightUnit implements IMeasureableUnit {
        KILOGRAM, GRAM, POUND;

        @Override
        public String getUnitName() {
            return this.name();
        }

        @Override
        public String getUnitType() {
            return this.getClass().getSimpleName();
        }
    }

    public enum TemperatureUnit implements IMeasureableUnit {
        KELVIN, CELSIUS, FAHRENHEIT;

        @Override
        public String getUnitName() {
            return this.name();
        }

        @Override
        public String getUnitType() {
            return this.getClass().getSimpleName();
        }
    }

    public enum VolumeUnit implements IMeasureableUnit {
        LITRE, MILLILITRE, GALLON;

        @Override
        public String getUnitName() {
            return this.name();
        }

        @Override
        public String getUnitType() {
            return this.getClass().getSimpleName();
        }
    }

    @Override
    public String toString() {
        return "QuantityDTO{" +
                "value=" + value +
                ", unit='" + unit + '\'' +
                ", measurmentType='" + measurmentType + '\'' +
                '}';
    }
    //    public static void main(String[] args) {
//    /*
//    this is for testting purspose here my using className.UnitType.unitName
//    we can get unitName like(meter,gram,kelvin) and unit typle like (LengthUnit,WeightUnit )
//     */
//        System.out.println(LengthUnit.METERS.getUnitType());
//        System.out.println(LengthUnit.METERS.getUnitName());
//
//        System.out.println(TemperatureUnit.KELVIN.getUnitType());
//        System.out.println(TemperatureUnit.KELVIN.getUnitName());
//
//        QuantityDTO quantityDTO = new QuantityDTO(10, TemperatureUnit.CELSIUS.getUnitName(), TemperatureUnit.KELVIN.getUnitType());
//        System.out.println(quantityDTO);
//    }
}
