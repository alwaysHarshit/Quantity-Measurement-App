package com.app.quantity_measurement_app.service;

import com.app.quantity_measurement_app.model.QuantityDTO;
import com.app.quantity_measurement_app.model.QuantityMeasurementDTO;

import java.util.List;

public interface IQuantityMeasurementService {

    QuantityMeasurementDTO compare(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

    QuantityMeasurementDTO convert(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

    QuantityMeasurementDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

    QuantityMeasurementDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO, QuantityDTO targetUnitDTO);

    QuantityMeasurementDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

    QuantityMeasurementDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO, QuantityDTO targetUnitDTO);

    QuantityMeasurementDTO divide(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

    List<QuantityMeasurementDTO> getOperationHistory(String operation);

    List<QuantityMeasurementDTO> getMeasurementsByType(String type);

    List<QuantityMeasurementDTO> getErrorHistory();

}
