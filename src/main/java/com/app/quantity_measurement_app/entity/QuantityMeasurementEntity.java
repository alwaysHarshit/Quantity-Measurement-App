package com.app.quantity_measurement_app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "quantity_measurement_entity")
@Data
public final class QuantityMeasurementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "this_value", nullable = false)
    private double thisValue;
    @Column(name = "this_unit", nullable = false)
    private String thisUnit;
    @Column(name = "this_measurement_type", nullable = false)
    private String thisMeasurementType;

    @Column(name = "that_value", nullable = false)
    private double thatValue;
    @Column(name = "that_unit", nullable = false)
    private String thatUnit;
    @Column(name = "that_measurement_type", nullable = false)
    private String thatMeasurementType;

    private String operation;

    @Column(name = "result_value")
    private double resultValue;
    @Column(name = "result_unit")
    private String resultUnit;
    @Column(name = "result_measurement_type")
    private String resultMeasurementType;

    @Column(name = "is_error")
    private boolean isError;
    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate(){
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
    }
    @PreUpdate
    public void onUpdate(){
        this.updatedAt=LocalDateTime.now();
    }

}
