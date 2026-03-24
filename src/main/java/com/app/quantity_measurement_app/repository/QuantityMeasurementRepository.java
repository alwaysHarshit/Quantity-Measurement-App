package com.app.quantity_measurement_app.repository;

import com.app.quantity_measurement_app.entity.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity,Long> {

    List<QuantityMeasurementEntity> findByOperation(String operation);

    // Match against the stored input/result measurement type fields.
    @Query("""
            SELECT e FROM QuantityMeasurementEntity e
            WHERE UPPER(e.thisMeasurementType) = UPPER(:measurementType)
               OR UPPER(e.thatMeasurementType) = UPPER(:measurementType)
               OR UPPER(e.resultMeasurementType) = UPPER(:measurementType)
            """)
    List<QuantityMeasurementEntity> findByMeasurementType(@Param("measurementType") String measurementType);

    // Find measurements created after specific date
    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime date);

    // Custom JPQL query for complex operations
    @Query("SELECT e FROM QuantityMeasurementEntity e WHERE e.operation = :operation AND e.isError = false")
    List<QuantityMeasurementEntity> findSuccessfulOperations(@Param("operation") String operation);

    // Count successful operations
    long countByOperationAndIsErrorFalse(String operation);

    // Find measurements with errors
    List<QuantityMeasurementEntity> findByIsErrorTrue();

}
