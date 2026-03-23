package com.apps.quantitymeasurement.exceptions;

public class DatabaseExceptions extends QuantityMeasurementException {

    public DatabaseExceptions(String msg, Throwable casuse) {
        super(msg, casuse);
    }

    public static DatabaseExceptions connectionFailed(String detail, Throwable casuse) {
        return new DatabaseExceptions("DB connection fails:" + detail, casuse);
    }

    public static DatabaseExceptions queryFailed(String query, Throwable cause) {
        return new DatabaseExceptions("Query execution failed: " + query, cause);
    }


    public static DatabaseExceptions transactionFailed(String operation, Throwable cause) {
        return new DatabaseExceptions("Transaction failed during " + operation, cause);
    }


}
