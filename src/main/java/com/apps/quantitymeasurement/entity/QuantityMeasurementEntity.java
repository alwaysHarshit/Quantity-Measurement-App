package com.apps.quantitymeasurement.entity;

import java.util.Objects;

public final class QuantityMeasurementEntity {
    private final String operation;
    private final String leftOperand;
    private final String rightOperand;
    private final String result;
    private final String errorMessage;

    public QuantityMeasurementEntity(String operation, String leftOperand, String rightOperand, String result) {
        this(operation, leftOperand, rightOperand, result, null);
    }

    public QuantityMeasurementEntity(String operation, String operand, String result) {
        this(operation, operand, null, result, null);
    }

    public QuantityMeasurementEntity(String operation, String leftOperand, String rightOperand, String result, String errorMessage) {
        this.operation = Objects.requireNonNull(operation, "Operation cannot be null");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public static QuantityMeasurementEntity error(String operation, String leftOperand, String rightOperand, String errorMessage) {
        return new QuantityMeasurementEntity(operation, leftOperand, rightOperand, null, errorMessage);
    }

    public String getOperation() {
        return operation;
    }

    public String getLeftOperand() {
        return leftOperand;
    }

    public String getRightOperand() {
        return rightOperand;
    }

    public String getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null;
    }

    @Override
    public String toString() {
        if (hasError()) {
            return "QuantityMeasurementEntity{" +
                    "operation='" + operation + '\'' +
                    ", leftOperand='" + leftOperand + '\'' +
                    ", rightOperand='" + rightOperand + '\'' +
                    ", errorMessage='" + errorMessage + '\'' +
                    '}';
        }

        return "QuantityMeasurementEntity{" +
                "operation='" + operation + '\'' +
                ", leftOperand='" + leftOperand + '\'' +
                ", rightOperand='" + rightOperand + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
