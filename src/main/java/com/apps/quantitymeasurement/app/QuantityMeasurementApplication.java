package com.apps.quantitymeasurement.app;

import com.apps.quantitymeasurement.controller.QuantityMeasurementController;
import com.apps.quantitymeasurement.dto.QuantityDTO;
import com.apps.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.apps.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.apps.quantitymeasurement.service.QuantityMeasurementServiceImpl;

public class QuantityMeasurementApplication {
    private static QuantityMeasurementApplication applicationInstance;
    private final QuantityMeasurementController controller;


    private QuantityMeasurementApplication() {

        IQuantityMeasurementRepository repository = QuantityMeasurementCacheRepository.getInstance();

        QuantityMeasurementServiceImpl service = new QuantityMeasurementServiceImpl(repository);

        this.controller = new QuantityMeasurementController(service);
    }

    public static QuantityMeasurementApplication getInstance(){
        if(applicationInstance==null) {
            applicationInstance = new QuantityMeasurementApplication();
        }
        return applicationInstance;
    }

    public QuantityMeasurementController getController() {
        return controller;
    }

    public static void main(String[] args) {
        QuantityMeasurementApplication application = QuantityMeasurementApplication.getInstance();

        QuantityDTO quantity1 = new QuantityDTO(1, QuantityDTO.LengthUnit.FEET);
        QuantityDTO quantity2 = new QuantityDTO(12, QuantityDTO.LengthUnit.INCHES);

        QuantityDTO result = application.getController().performAddition(quantity1, quantity2);
        application.getController().handleResult(quantity1, quantity2, "+", result);
    }
}
