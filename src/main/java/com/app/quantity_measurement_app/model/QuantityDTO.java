package com.app.quantity_measurement_app.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.logging.Logger;

interface IMeasureableUnit {

    String getUnitName();
    String getmeasurementType();
}


@Schema(description = "A quantity with values and unit",examples = {
        "3 feet + 4 fee",
        "5 feet to cm ",
        "is 12cm equal to 1 feet"
})
@Data
public final class QuantityDTO {
private static Logger logger=Logger.getLogger(QuantityDTO.class.getName());
    @NotNull(message = "Value cannot be empty")
    @Schema(example ="1.0")
    private double value;

    @NotNull(message = "Unit cannot be null")
    @Schema(example = "FEET", allowableValues = {
            "FEET", "INCHES", "YARDS", "CENTIMETERS", "LITRE", "MILLILITER", "GALLON",
            "MILLIGRAM", "GRAM", "KILOGRAM", "POUND", "TONNE","CELSIUS", "FAHRENHEIT"
    })
    private String unit;

    @NotNull(message = "Measurement type cannot be null")
    @Pattern(regexp = "LengthUnit|VolumeUnit|WeightUnit|TemperatureUnit",
            message = "Measurement type must be one of: LengthUnit, VolumeUnit,WeightUnit, TemperatureUnit")
    @Schema(example = "LengthUnit", allowableValues = {"LengthUnit", "VolumeUnit", "WeightUnit", "TemperatureUnit"})
    private String measurementType;

    @AssertTrue(message = "Unit must be valid for the specified measurement type")
    public boolean isValidUnit() {
        logger.info("Validating unit: " + unit + " for measurement type: " + measurementType);

        try {
            switch (measurementType) {
                case "LengthUnit":
                    LengthUnit.valueOf(unit);
                    break;
                case "VolumeUnit":
                    VolumeUnit.valueOf(unit);
                    break;
                case "WeightUnit":
                    WeightUnit.valueOf(unit);
                    break;
                case "TemperatureUnit":
                    TemperatureUnit.valueOf(unit);
                    break;
                default:
                    return false;
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;

    }

    public enum LengthUnit implements IMeasureableUnit {
        FEET, METERS, CENTIMETERS, INCHES, YARDS;

        @Override
        public String getUnitName() {
            return this.name();
        }

        @Override
        public String getmeasurementType() {
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
        public String getmeasurementType() {
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
        public String getmeasurementType() {
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
        public String getmeasurementType() {
            return this.getClass().getSimpleName();
        }
    }


}
