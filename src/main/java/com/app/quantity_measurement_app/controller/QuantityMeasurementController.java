package com.app.quantity_measurement_app.controller;

import com.app.quantity_measurement_app.model.QuantityInputDTO;
import com.app.quantity_measurement_app.model.QuantityMeasurementDTO;
import com.app.quantity_measurement_app.service.IQuantityMeasurementService;


import com.app.quantity_measurement_app.service.QuantityMeasurementServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/quantity")
@Validated
public class QuantityMeasurementController {

    @Autowired
    private QuantityMeasurementServiceImpl service;

    // ------------------ JSON EXAMPLES ------------------

    private static final String EX_FEET_INCH = """
        {
          "thisQuantityDTO": {"value":1.0,"unit":"FEET","measurementType":"LengthUnit"},
          "thatQuantityDTO": {"value":12.0,"unit":"INCHES","measurementType":"LengthUnit"}
        }
        """;

    private static final String EX_YARD_FEET = """
        {
          "thisQuantityDTO": {"value":1.0,"unit":"YARDS","measurementType":"LengthUnit"},
          "thatQuantityDTO": {"value":3.0,"unit":"FEET","measurementType":"LengthUnit"}
        }
        """;

    private static final String EX_GALLON_LITRE = """
        {
          "thisQuantityDTO": {"value":1.0,"unit":"GALLON","measurementType":"VolumeUnit"},
          "thatQuantityDTO": {"value":3.785,"unit":"LITRE","measurementType":"VolumeUnit"}
        }
        """;

    private static final String EX_TEMP = """
        {
          "thisQuantityDTO": {"value":212.0,"unit":"FAHRENHEIT","measurementType":"TemperatureUnit"},
          "thatQuantityDTO": {"value":100.0,"unit":"CELSIUS","measurementType":"TemperatureUnit"}
        }
        """;

    private static final String EX_WITH_TARGET = """
        {
          "thisQuantityDTO": {"value":1.0,"unit":"FEET","measurementType":"LengthUnit"},
          "thatQuantityDTO": {"value":12.0,"unit":"INCHES","measurementType":"LengthUnit"},
          "targetQuantityDTO": {"value":0.0,"unit":"INCHES","measurementType":"LengthUnit"}
        }
        """;

    // ------------------ COMPARE ------------------

    @PostMapping("/compare")
    @Operation(
            summary = "Compare two quantities",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = {
                            @ExampleObject(name = "Feet = 12 Inches", value = EX_FEET_INCH),
                            @ExampleObject(name = "Yard = 3 Feet", value = EX_YARD_FEET),
                            @ExampleObject(name = "Gallon = Litres", value = EX_GALLON_LITRE),
                            @ExampleObject(name = "212°F = 100°C", value = EX_TEMP)
                    })
            )
    )
    public ResponseEntity<QuantityMeasurementDTO> performComparison(
            @Valid @RequestBody QuantityInputDTO  inputDTO) {

        return ResponseEntity.ok(service.compare(inputDTO.getThisQuantityDTO(),inputDTO.getThatQuantityDTO()));
    }

    // ------------------ CONVERT ------------------

    @PostMapping("/convert")
    @Operation(
            summary = "Convert quantity to target unit",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = {
                            @ExampleObject(name = "Feet -> Inches", value = EX_FEET_INCH),
                            @ExampleObject(name = "Yard -> Feet", value = EX_YARD_FEET),
                            @ExampleObject(name = "Gallon -> Litres", value = EX_GALLON_LITRE),
                            @ExampleObject(name = "212°F -> 100°C", value = EX_TEMP)
                    })
            )
    )
    public ResponseEntity<QuantityMeasurementDTO> performConversion(
            @Valid @RequestBody QuantityInputDTO inputDTO) {

        return ResponseEntity.ok(service.convert(inputDTO.getThisQuantityDTO(),inputDTO.getThatQuantityDTO()));
    }

    // ------------------ ADD ------------------

    @PostMapping("/add")
    @Operation(
            summary = "Add two quantities",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = {
                            @ExampleObject(name = "Feet + Inches", value = EX_FEET_INCH),
                            @ExampleObject(name = "Yard + Feet", value = EX_YARD_FEET),
                            @ExampleObject(name = "Gallon + Litres", value = EX_GALLON_LITRE)
                    })
            )
    )
    public ResponseEntity<QuantityMeasurementDTO> performAddition(
            @Valid @RequestBody QuantityInputDTO inputDTO) {

        return ResponseEntity.ok(service.add(inputDTO.getThisQuantityDTO(),inputDTO.getThatQuantityDTO()));
    }

    // ------------------ ADD WITH TARGET ------------------

    @PostMapping("/add-with-target-unit")
    @Operation(
            summary = "Add two quantities with target unit",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = {
                            @ExampleObject(name = "Feet + Inches (Target)", value = EX_WITH_TARGET)
                    })
            )
    )
    public ResponseEntity<QuantityMeasurementDTO> performAdditionWithTargetUnit(
            @Valid @RequestBody QuantityInputDTO  inputDTO) {

        return ResponseEntity.ok(service.add(inputDTO.getThisQuantityDTO(),inputDTO.getThatQuantityDTO(),inputDTO.getTargetQuantityDTO()));
    }

    // ------------------ SUBTRACT ------------------

    @PostMapping("/subtract")
    @Operation(
            summary = "Subtract two quantities",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = {
                            @ExampleObject(name = "Feet - Inches", value = EX_FEET_INCH),
                            @ExampleObject(name = "Yard - Feet", value = EX_YARD_FEET),
                            @ExampleObject(name = "Gallon - Litres", value = EX_GALLON_LITRE)
                    })
            )
    )
    public ResponseEntity<QuantityMeasurementDTO> performSubtraction(
            @Valid @RequestBody QuantityInputDTO quantityInputDTO) {

        return ResponseEntity.ok(service.subtract(quantityInputDTO.getThisQuantityDTO(),quantityInputDTO.getThatQuantityDTO()));
    }

    // ------------------ SUBTRACT WITH TARGET ------------------

    @PostMapping("/subtract-with-target-unit")
    @Operation(
            summary = "Subtract with target unit",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = {
                            @ExampleObject(name = "Feet - Inches (Target)", value = EX_WITH_TARGET)
                    })
            )
    )
    public ResponseEntity<QuantityMeasurementDTO> performSubtractionWithTargetUnit(
            @Valid @RequestBody QuantityInputDTO quantityInputDTO) {

        return ResponseEntity.ok(service.subtract(quantityInputDTO.getThisQuantityDTO(),quantityInputDTO.getThatQuantityDTO(),quantityInputDTO.getTargetQuantityDTO()));
    }

    // ------------------ DIVIDE ------------------

    @PostMapping("/divide")
    @Operation(
            summary = "Divide two quantities",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = {
                            @ExampleObject(name = "Feet / Inches", value = EX_FEET_INCH),
                            @ExampleObject(name = "Yard / Feet", value = EX_YARD_FEET),
                            @ExampleObject(name = "Gallon / Litres", value = EX_GALLON_LITRE)
                    })
            )
    )
    public ResponseEntity<QuantityMeasurementDTO> performDivision(
            @Valid @RequestBody QuantityInputDTO quantityInputDTO) {

        return ResponseEntity.ok(service.divide(quantityInputDTO.getThisQuantityDTO(),quantityInputDTO.getThatQuantityDTO()));
    }

    // ------------------ HISTORY ------------------

    @GetMapping("/history/operation/{operation}")
    @Operation(summary = "Get operation history")
    public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistory(
            @PathVariable String operation) {

        return ResponseEntity.ok(service.getOperationHistory(operation));
    }

    @GetMapping("/history/type/{type}")
    @Operation(summary = "Get history by type")
    public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistoryByType(
            @PathVariable String type) {

        return ResponseEntity.ok(service.getMeasurementsByType(type));
    }
    @GetMapping("/history/errored")
    @Operation(summary = "Get errored operations history")
    public ResponseEntity<List<QuantityMeasurementDTO>> getErroredOperations() {

        return ResponseEntity.ok(service.getErrorHistory());
    }
}