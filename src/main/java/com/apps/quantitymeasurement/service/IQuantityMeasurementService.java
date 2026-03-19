package com.apps.quantitymeasurement.service;

import com.apps.quantitymeasurement.dto.QuantityDTO;

public interface IQuantityMeasurementService {

    QuantityDTO add(QuantityDTO thisQuantity, QuantityDTO thatQuantity);

    boolean compare(QuantityDTO thisQuantity, QuantityDTO thatQuantity);

    QuantityDTO convert(QuantityDTO thisQuantity, QuantityDTO thatQuantity);

    double divide(QuantityDTO thisQuantity, QuantityDTO thatQuantity);

    QuantityDTO subtract(QuantityDTO thisQuantity, QuantityDTO thatQuantity);
}
