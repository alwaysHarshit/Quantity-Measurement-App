package com.apps.quantitymeasurement.exceptions;

public class QuantityMeasurementException extends RuntimeException{

    public QuantityMeasurementException(String msg,Throwable casuse){
        super(msg,casuse);
    }
}
