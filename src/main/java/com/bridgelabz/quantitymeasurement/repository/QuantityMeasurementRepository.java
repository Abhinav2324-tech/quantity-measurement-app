
package com.bridgelabz.quantitymeasurement.repository;

import com.bridgelabz.quantitymeasurement.entity.QuantityJpaEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuantityMeasurementRepository
        extends JpaRepository<QuantityJpaEntity, Long> {

    List<QuantityJpaEntity> findByOperation(String operation);

    List<QuantityJpaEntity> findByFirstMeasurementType(
            String measurementType);

    List<QuantityJpaEntity> findByCreatedAtAfter(
            LocalDateTime date);

    List<QuantityJpaEntity> findByErrorTrue();

    long countByOperationAndErrorFalse(String operation);
}
