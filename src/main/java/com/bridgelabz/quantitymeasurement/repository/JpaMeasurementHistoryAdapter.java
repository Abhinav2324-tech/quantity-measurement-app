
package com.bridgelabz.quantitymeasurement.repository;

import com.bridgelabz.quantitymeasurement.entity.QuantityJpaEntity;
import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JpaMeasurementHistoryAdapter
        implements IQuantityMeasurementRepository {

    private final QuantityMeasurementRepository repository;

    public JpaMeasurementHistoryAdapter(
            QuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        repository.save(QuantityJpaEntity.from(entity));
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return repository.findAll()
                .stream()
                .map(QuantityJpaEntity::toDomainEntity)
                .toList();
    }

    @Override
    public List<QuantityMeasurementEntity>
    getMeasurementsByOperation(String operation) {
        return repository.findByOperation(operation.toUpperCase())
                .stream()
                .map(QuantityJpaEntity::toDomainEntity)
                .toList();
    }

    @Override
    public List<QuantityMeasurementEntity>
    getMeasurementsByType(String type) {
        return repository.findByFirstMeasurementType(
                        type.toUpperCase())
                .stream()
                .map(QuantityJpaEntity::toDomainEntity)
                .toList();
    }

    @Override
    public long getTotalCount() {
        return repository.count();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
