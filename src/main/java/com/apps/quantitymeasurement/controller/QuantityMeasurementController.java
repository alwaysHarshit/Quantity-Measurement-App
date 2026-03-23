package com.apps.quantitymeasurement.controller;

import com.apps.quantitymeasurement.app.QuantityMeasurementApplication;
import com.apps.quantitymeasurement.dto.QuantityDTO;
import com.apps.quantitymeasurement.service.IQuantityMeasurementService;

import java.util.logging.Logger;

public class QuantityMeasurementController {

    private static final Logger LOGGER = Logger.getLogger(QuantityMeasurementController.class.getName());

    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        this.service = service;
        LOGGER.info("Controller wired with service: " + service.getClass().getSimpleName());
    }

    //arthimatic operations
    public QuantityDTO performAddition(QuantityDTO thisQuantity,QuantityDTO thatQuantity){
        return service.add(thisQuantity,thatQuantity);
    }
    public QuantityDTO performSubtraction(QuantityDTO thisQuantity,QuantityDTO thatQuantity){
        return service.subtract(thisQuantity,thatQuantity);
    }
    public double performDivision(QuantityDTO thisQuantity,QuantityDTO thatQuantity){
        return service.divide(thisQuantity,thatQuantity);
    }

    //comparoison and conversion
    public boolean performComparison(QuantityDTO thisQuantity,QuantityDTO thatQuantity){
        return service.compare(thisQuantity,thatQuantity);
    }

    public QuantityDTO performConverstion(QuantityDTO thisQuantity,QuantityDTO thatQuantity){
        return service.convert(thisQuantity,thatQuantity);
    }

    public QuantityDTO performConversion(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
        return service.convert(thisQuantity, thatQuantity);
    }

    public void handleResult(QuantityDTO resultDTO) {
        System.out.printf("%.1f %s%n", resultDTO.getValue(), resultDTO.getUnit());
    }

    public String handleResult(QuantityDTO thisQuantity, QuantityDTO thatQuantity, String operator, QuantityDTO resultDTO) {
       return String.format("%.1f %s %s %.1f %s = %.1f %s%n"
               ,thisQuantity.getValue(), thisQuantity.getUnit(),
               operator,
               thatQuantity.getValue(), thatQuantity.getUnit(),
               resultDTO.getValue(), resultDTO.getUnit()
                );
    }
}
