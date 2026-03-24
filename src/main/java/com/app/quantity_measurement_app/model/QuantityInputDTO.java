package com.app.quantity_measurement_app.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(
        description = "Input object containing two quantities and an optional target quantity",
        example = """
    {
      "thisQuantityDTO": {
        "value": 1.0,
        "unit": "FEET",
        "measurementType": "LengthUnit"
      },
      "thatQuantityDTO": {
        "value": 12.0,
        "unit": "INCHES",
        "measurementType": "LengthUnit"
      },
      "targetQuantityDTO": {
        "value": 0.0,
        "unit": "INCHES",
        "measurementType": "LengthUnit"
      }
    }
    """
)
public class QuantityInputDTO {

    @Valid
    @NotNull(message = "First quantity cannot be null")
    private QuantityDTO thisQuantityDTO;

    @Valid
    @NotNull(message = "Second quantity cannot be null")
    private QuantityDTO thatQuantityDTO;

    // Optional field (used in add/subtract with target)
    @Valid
    @Schema(nullable = true, description = "Optional target unit for result")
    private QuantityDTO targetQuantityDTO;
}