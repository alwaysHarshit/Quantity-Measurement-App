package com.app.quantity_measurement_app.model;


import com.app.quantity_measurement_app.entity.QuantityMeasurementEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuantityMeasurementDTO {
    public double thisValue;
    public String thisUnit;
    public String thisMeasurementType;

    public double thatValue;
    public String thatUnit;
    public String thatMeasurementType;

    public String operation;

    public double resultValue;
    public String resultUnit;
    public String resultMeasurementType;

    @JsonProperty("error")
    public boolean error;
    public String errorMessage;

    public static QuantityMeasurementDTO from(QuantityMeasurementEntity quantityMeasurementEntity) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.thisValue = quantityMeasurementEntity.getThisValue();
        dto.thisUnit = quantityMeasurementEntity.getThisUnit();
        dto.thisMeasurementType = quantityMeasurementEntity.getThisMeasurementType();

        dto.thatValue = quantityMeasurementEntity.getThatValue();
        dto.thatUnit = quantityMeasurementEntity.getThatUnit();
        dto.thatMeasurementType = quantityMeasurementEntity.getThatMeasurementType();

        dto.operation = quantityMeasurementEntity.getOperation();

        dto.resultValue = quantityMeasurementEntity.getResultValue();
        dto.resultUnit = quantityMeasurementEntity.getResultUnit();
        dto.resultMeasurementType = quantityMeasurementEntity.getResultMeasurementType();

        dto.error = quantityMeasurementEntity.isError();
        dto.errorMessage = quantityMeasurementEntity.getErrorMessage();
        return dto;
    }

    public QuantityMeasurementEntity toEntity() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(thisValue);
        entity.setThisUnit(thisUnit);
        entity.setThisMeasurementType(thisMeasurementType);

        entity.setThatValue(thatValue);
        entity.setThatUnit(thatUnit);
        entity.setThatMeasurementType(thatMeasurementType);

        entity.setOperation(operation);

        entity.setResultValue(resultValue);
        entity.setResultUnit(resultUnit);
        entity.setResultMeasurementType(resultMeasurementType);

        entity.setError(error);
        entity.setErrorMessage(errorMessage);
        return entity;
    }

    public static List<QuantityMeasurementDTO> fromEntityList(List<QuantityMeasurementEntity> entities) {
        return entities.stream()
                .map(QuantityMeasurementDTO::from)
                .toList();
    }

    public static List<QuantityMeasurementEntity> toEntityList(List<QuantityMeasurementDTO> dtos) {
        return dtos.stream()
                .map(QuantityMeasurementDTO::toEntity)
                .toList();
    }

}

