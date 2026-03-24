package com.app.quantity_measurement_app.service;

import com.app.quantity_measurement_app.entity.QuantityMeasurementEntity;
import com.app.quantity_measurement_app.model.OperationType;
import com.app.quantity_measurement_app.model.QuantityDTO;
import com.app.quantity_measurement_app.model.QuantityMeasurementDTO;
import com.app.quantity_measurement_app.repository.QuantityMeasurementRepository;
import com.app.quantity_measurement_app.units.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.logging.Logger;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger LOGGER = Logger.getLogger(QuantityMeasurementServiceImpl.class.getName());

    @Autowired
    private QuantityMeasurementRepository repository;

    private static final Map<String, LengthUnit> LENGTH_UNIT_MAP = Map.of(
            "FEET", LengthUnit.FEET,
            "INCHES", LengthUnit.INCHES,
            "YARDS", LengthUnit.YARDS,
            "CENTIMETERS", LengthUnit.CM
    );

    // =========================================================================
    // UNIT MAPS  —  String → Enum, created once, reused forever
    // =========================================================================
    private static final Map<String, WeightUnit> WEIGHT_UNIT_MAP = Map.of(
            "KILOGRAM", WeightUnit.KILOGRAM,
            "GRAM", WeightUnit.GRAM,
            "POUND", WeightUnit.POUND
    );
    private static final Map<String, VolumeUnit> VOLUME_UNIT_MAP = Map.of(
            "LITRE", VolumeUnit.LITRE,
            "MILLILITRE", VolumeUnit.MILLILITRE,
            "GALLON", VolumeUnit.GALLON
    );
    private static final Map<String, TemperatureUnit> TEMPERATURE_UNIT_MAP = Map.of(
            "CELSIUS", TemperatureUnit.CELSIUS,
            "FAHRENHEIT", TemperatureUnit.FAHRENHEIT
    );


    // =========================================================================
    // PUBLIC API
    // =========================================================================

    @Override
    public QuantityMeasurementDTO compare(QuantityDTO a, QuantityDTO b) {

        Boolean isEqual = null;
        String errorMessage = null;

        try {
            isEqual = switch (a.getMeasurementType()) {
                case "LengthUnit" -> toLengthQuantity(a).equals(toLengthQuantity(b));
                case "WeightUnit" -> toWeightQuantity(a).equals(toWeightQuantity(b));
                case "VolumeUnit" -> toVolumeQuantity(a).equals(toVolumeQuantity(b));
                case "TemperatureUnit" -> toTemperatureQuantity(a).equals(toTemperatureQuantity(b));
                default -> throw new IllegalArgumentException("Unknown type: " + a.getMeasurementType());
            };

        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
            LOGGER.warning("COMPARE failed: " + e.getMessage());
        }

        // encode boolean as 1.0 / 0.0 so it fits the existing resultValue field
        QuantityDTO result = isEqual != null ? buildBooleanResult(isEqual) : null;

        return saveAndReturn(buildMeasurementDTO(a, b, result, OperationType.COMPARE, errorMessage));
    }

    @Override
    public QuantityMeasurementDTO convert(QuantityDTO a, QuantityDTO b) {

        QuantityDTO result = null;
        String errorMessage = null;

        try {
            result = switch (a.getMeasurementType()) {
                case "LengthUnit" ->
                        performOperation(a, b, this::toLengthQuantity, (q1, q2) -> q1.convertTo(resolveUnit(LENGTH_UNIT_MAP, b.getUnit())), "LengthUnit");
                case "WeightUnit" ->
                        performOperation(a, b, this::toWeightQuantity, (q1, q2) -> q1.convertTo(resolveUnit(WEIGHT_UNIT_MAP, b.getUnit())), "WeightUnit");
                case "VolumeUnit" ->
                        performOperation(a, b, this::toVolumeQuantity, (q1, q2) -> q1.convertTo(resolveUnit(VOLUME_UNIT_MAP, b.getUnit())), "VolumeUnit");
                case "TemperatureUnit" ->
                        performOperation(a, b, this::toTemperatureQuantity, (q1, q2) -> q1.convertTo(resolveUnit(TEMPERATURE_UNIT_MAP, b.getUnit())), "TemperatureUnit");
                default -> throw new IllegalArgumentException("Unknown type: " + a.getMeasurementType());
            };

        } catch (IllegalArgumentException | ArithmeticException e) {
            errorMessage = e.getMessage();
            LOGGER.warning("CONVERT failed: " + e.getMessage());
        }

        return saveAndReturn(buildMeasurementDTO(a, b, result, OperationType.CONVERT, errorMessage));
    }

    @Override
    public QuantityMeasurementDTO add(QuantityDTO a, QuantityDTO b) {

        QuantityDTO result = null;
        String errorMessage = null;

        try {
            result = switch (a.getMeasurementType()) {
                case "LengthUnit" ->
                        performOperation(a, b, this::toLengthQuantity, (q1, q2) -> q1.add(q2), "LengthUnit");
                case "WeightUnit" ->
                        performOperation(a, b, this::toWeightQuantity, (q1, q2) -> q1.add(q2), "WeightUnit");
                case "VolumeUnit" ->
                        performOperation(a, b, this::toVolumeQuantity, (q1, q2) -> q1.add(q2), "VolumeUnit");
                case "TemperatureUnit" ->
                        performOperation(a, b, this::toTemperatureQuantity, (q1, q2) -> q1.add(q2), "TemperatureUnit");
                default -> throw new IllegalArgumentException("Unknown type: " + a.getMeasurementType());
            };

        } catch (IllegalArgumentException | ArithmeticException e) {
            errorMessage = e.getMessage();
            LOGGER.warning("ADD failed: " + e.getMessage());
        }

        return saveAndReturn(buildMeasurementDTO(a, b, result, OperationType.ADD, errorMessage));
    }

    @Override
    public QuantityMeasurementDTO add(QuantityDTO a, QuantityDTO b, QuantityDTO targetUnitDTO) {

        QuantityDTO result = null;
        String errorMessage = null;

        try {
            result = switch (a.getMeasurementType()) {
                case "LengthUnit" ->
                        performOperation(a, b, this::toLengthQuantity, (q1, q2) -> q1.add(q2, resolveUnit(LENGTH_UNIT_MAP, targetUnitDTO.getUnit())), "LengthUnit");
                case "WeightUnit" ->
                        performOperation(a, b, this::toWeightQuantity, (q1, q2) -> q1.add(q2, resolveUnit(WEIGHT_UNIT_MAP, targetUnitDTO.getUnit())), "WeightUnit");
                case "VolumeUnit" ->
                        performOperation(a, b, this::toVolumeQuantity, (q1, q2) -> q1.add(q2, resolveUnit(VOLUME_UNIT_MAP, targetUnitDTO.getUnit())), "VolumeUnit");
                case "TemperatureUnit" ->
                        performOperation(a, b, this::toTemperatureQuantity, (q1, q2) -> q1.add(q2, resolveUnit(TEMPERATURE_UNIT_MAP, targetUnitDTO.getUnit())), "TemperatureUnit");
                default -> throw new IllegalArgumentException("Unknown type: " + a.getMeasurementType());
            };

        } catch (IllegalArgumentException | ArithmeticException e) {
            errorMessage = e.getMessage();
            LOGGER.warning("ADD (with target) failed: " + e.getMessage());
        }

        return saveAndReturn(buildMeasurementDTO(a, b, result, OperationType.ADD, errorMessage));
    }

    @Override
    public QuantityMeasurementDTO subtract(QuantityDTO a, QuantityDTO b) {

        QuantityDTO result = null;
        String errorMessage = null;

        try {
            result = switch (a.getMeasurementType()) {
                case "LengthUnit" ->
                        performOperation(a, b, this::toLengthQuantity, (q1, q2) -> q1.subtract(q2), "LengthUnit");
                case "WeightUnit" ->
                        performOperation(a, b, this::toWeightQuantity, (q1, q2) -> q1.subtract(q2), "WeightUnit");
                case "VolumeUnit" ->
                        performOperation(a, b, this::toVolumeQuantity, (q1, q2) -> q1.subtract(q2), "VolumeUnit");
                case "TemperatureUnit" ->
                        performOperation(a, b, this::toTemperatureQuantity, (q1, q2) -> q1.subtract(q2), "TemperatureUnit");
                default -> throw new IllegalArgumentException("Unknown type: " + a.getMeasurementType());
            };

        } catch (IllegalArgumentException | ArithmeticException e) {
            errorMessage = e.getMessage();
            LOGGER.warning("SUBTRACT failed: " + e.getMessage());
        }

        return saveAndReturn(buildMeasurementDTO(a, b, result, OperationType.SUBTRACT, errorMessage));
    }

    @Override
    public QuantityMeasurementDTO subtract(QuantityDTO a, QuantityDTO b, QuantityDTO targetUnitDTO) {

        QuantityDTO result = null;
        String errorMessage = null;

        try {
            result = switch (a.getMeasurementType()) {
                case "LengthUnit" ->
                        performOperation(a, b, this::toLengthQuantity, (q1, q2) -> q1.subtract(q2, resolveUnit(LENGTH_UNIT_MAP, targetUnitDTO.getUnit())), "LengthUnit");
                case "WeightUnit" ->
                        performOperation(a, b, this::toWeightQuantity, (q1, q2) -> q1.subtract(q2, resolveUnit(WEIGHT_UNIT_MAP, targetUnitDTO.getUnit())), "WeightUnit");
                case "VolumeUnit" ->
                        performOperation(a, b, this::toVolumeQuantity, (q1, q2) -> q1.subtract(q2, resolveUnit(VOLUME_UNIT_MAP, targetUnitDTO.getUnit())), "VolumeUnit");
                case "TemperatureUnit" ->
                        performOperation(a, b, this::toTemperatureQuantity, (q1, q2) -> q1.subtract(q2, resolveUnit(TEMPERATURE_UNIT_MAP, targetUnitDTO.getUnit())), "TemperatureUnit");
                default -> throw new IllegalArgumentException("Unknown type: " + a.getMeasurementType());
            };

        } catch (IllegalArgumentException | ArithmeticException e) {
            errorMessage = e.getMessage();
            LOGGER.warning("SUBTRACT (with target) failed: " + e.getMessage());
        }

        return saveAndReturn(buildMeasurementDTO(a, b, result, OperationType.SUBTRACT, errorMessage));
    }

    @Override
    public QuantityMeasurementDTO divide(QuantityDTO a, QuantityDTO b) {

        QuantityDTO result = null;
        String errorMessage = null;

        try {
            result = switch (a.getMeasurementType()) {
                case "LengthUnit" ->
                        performOperation(a, b, this::toLengthQuantity, (q1, q2) -> q1.divide(q2), "LengthUnit");
                case "WeightUnit" ->
                        performOperation(a, b, this::toWeightQuantity, (q1, q2) -> q1.divide(q2), "WeightUnit");
                case "VolumeUnit" ->
                        performOperation(a, b, this::toVolumeQuantity, (q1, q2) -> q1.divide(q2), "VolumeUnit");
                case "TemperatureUnit" ->
                        performOperation(a, b, this::toTemperatureQuantity, (q1, q2) -> q1.divide(q2), "TemperatureUnit");
                default -> throw new IllegalArgumentException("Unknown type: " + a.getMeasurementType());
            };

        } catch (IllegalArgumentException | ArithmeticException e) {
            errorMessage = e.getMessage();
            LOGGER.warning("DIVIDE failed: " + e.getMessage());
        }

        return saveAndReturn(buildMeasurementDTO(a, b, result, OperationType.DIVIDE, errorMessage));
    }

    // =========================================================================
    // HISTORY QUERIES
    // =========================================================================

    @Override
    public List<QuantityMeasurementDTO> getOperationHistory(String operation) {
        List<QuantityMeasurementEntity> entities = repository.findByOperation(operation.toUpperCase());
        return QuantityMeasurementDTO.fromEntityList(entities);
    }

    @Override
    public List<QuantityMeasurementDTO> getMeasurementsByType(String type) {
        List<QuantityMeasurementEntity> entities = repository.findByMeasurementType(type);
        return QuantityMeasurementDTO.fromEntityList(entities);
    }

    @Override
    public List<QuantityMeasurementDTO> getErrorHistory() {
        List<QuantityMeasurementEntity> entities = repository.findByIsErrorTrue();
        return QuantityMeasurementDTO.fromEntityList(entities);
    }

    // =========================================================================
    // DTO CONVERTERS  —  QuantityDTO → QuantityClass<U>
    // =========================================================================

    public QuantityClass<LengthUnit> toLengthQuantity(QuantityDTO dto) {
        return new QuantityClass<>(dto.getValue(), resolveUnit(LENGTH_UNIT_MAP, dto.getUnit()));
    }

    public QuantityClass<WeightUnit> toWeightQuantity(QuantityDTO dto) {
        return new QuantityClass<>(dto.getValue(), resolveUnit(WEIGHT_UNIT_MAP, dto.getUnit()));
    }

    public QuantityClass<VolumeUnit> toVolumeQuantity(QuantityDTO dto) {
        return new QuantityClass<>(dto.getValue(), resolveUnit(VOLUME_UNIT_MAP, dto.getUnit()));
    }

    public QuantityClass<TemperatureUnit> toTemperatureQuantity(QuantityDTO dto) {
        return new QuantityClass<>(dto.getValue(), resolveUnit(TEMPERATURE_UNIT_MAP, dto.getUnit()));
    }

    // =========================================================================
    // PRIVATE HELPERS
    // =========================================================================

    /**
     * Resolves a unit string to its enum value.
     * Throws a clear error if the unit is not found in the map.
     */
    private <U extends IMeasurable> U resolveUnit(Map<String, U> map, String unit) {
        U resolved = map.get(unit.toUpperCase());
        if (resolved == null) {
            throw new IllegalArgumentException("Unsupported unit: " + unit);
        }
        return resolved;
    }

    /**
     * Core engine — converts both DTOs, applies the operation, returns result as DTO.
     * Used by add / subtract / divide / convert.
     */
    private <U extends IMeasurable> QuantityDTO performOperation(
            QuantityDTO a,
            QuantityDTO b,
            Function<QuantityDTO, QuantityClass<U>> converter,
            BinaryOperator<QuantityClass<U>> operator,
            String measurementType) {

        if (!a.getMeasurementType().equals(b.getMeasurementType())) {
            throw new IllegalArgumentException("Cannot operate on different measurement types");
        }

        QuantityClass<U> q1 = converter.apply(a);
        QuantityClass<U> q2 = converter.apply(b);
        QuantityClass<U> result = operator.apply(q1, q2);

        QuantityDTO dto = new QuantityDTO();
        dto.setValue(result.getValue());
        dto.setUnit(result.getUnit().getUnitName());
        dto.setMeasurementType(measurementType);
        return dto;
    }

    /**
     * Encodes a boolean comparison result as a QuantityDTO
     * so it fits the existing resultValue field.
     * 1.0 = equal, 0.0 = not equal.
     */
    private QuantityDTO buildBooleanResult(boolean isEqual) {
        QuantityDTO dto = new QuantityDTO();
        dto.setValue(isEqual ? 1.0 : 0.0);
        dto.setUnit("BOOLEAN");
        dto.setMeasurementType("COMPARE");
        return dto;
    }

    /**
     * Assembles the final QuantityMeasurementDTO from inputs + result/error.
     * result == null means the operation failed — error fields are set instead.
     */
    private QuantityMeasurementDTO buildMeasurementDTO(
            QuantityDTO a,
            QuantityDTO b,
            QuantityDTO result,
            OperationType operation,
            String errorMessage) {

        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();

        // inputs — always set
        dto.setThisValue(a.getValue());
        dto.setThisUnit(a.getUnit());
        dto.setThisMeasurementType(a.getMeasurementType());

        dto.setThatValue(b.getValue());
        dto.setThatUnit(b.getUnit());
        dto.setThatMeasurementType(b.getMeasurementType());

        dto.setOperation(operation.getDisplayName());

        // result OR error — mutually exclusive
        if (result != null) {
            dto.setResultValue(result.getValue());
            dto.setResultUnit(result.getUnit());
            dto.setResultMeasurementType(result.getMeasurementType());
            dto.setError(false);
            dto.setErrorMessage(null);
        } else {
            dto.setError(true);
            dto.setErrorMessage(errorMessage);
        }

        return dto;
    }
    /**
     * Saves the DTO to the database and returns it.
     * Single place that touches the repository for all write operations.
     */
    private QuantityMeasurementDTO saveAndReturn(QuantityMeasurementDTO dto) {
        repository.save(dto.toEntity());
        return dto;
    }

}