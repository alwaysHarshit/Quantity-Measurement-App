package com.apps.quantitymeasurement.repository;

import com.apps.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.ArrayList;
import java.util.List;

public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

    private static IQuantityMeasurementRepository cacheRepositoryInstance;
    private final List<QuantityMeasurementEntity> entities = new ArrayList<>();

    public static IQuantityMeasurementRepository getInstance() {

        if(cacheRepositoryInstance ==null){
            cacheRepositoryInstance =new QuantityMeasurementCacheRepository();
        }
        return cacheRepositoryInstance;
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        // A simple in-memory cache is enough until a real database-backed repository is introduced.
        entities.add(entity);
    }
}
