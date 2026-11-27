
package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.bridgelabz.quantitymeasurement.exception.QuantityMeasurementException;
import com.bridgelabz.quantitymeasurement.repository.JpaMeasurementHistoryAdapter;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpringQuantityMeasurementService {

    private final QuantityMeasurementServiceImpl delegate;
    private final JpaMeasurementHistoryAdapter repository;

    public SpringQuantityMeasurementService(
            JpaMeasurementHistoryAdapter repository) {

        this.repository = repository;
        this.delegate =
                new QuantityMeasurementServiceImpl(repository);
    }

    private QuantityMeasurementDTO response(
            QuantityMeasurementEntity entity) {

        if (entity.hasError()) {
            throw new QuantityMeasurementException(
                    entity.getErrorMessage());
        }

        return QuantityMeasurementDTO.fromEntity(entity);
    }

    public QuantityMeasurementDTO compare(
            QuantityDTO first, QuantityDTO second) {

        return response(delegate.compare(first, second));
    }

    public QuantityMeasurementDTO convert(
            QuantityDTO source, String targetUnit) {

        return response(delegate.convert(source, targetUnit));
    }

    public QuantityMeasurementDTO add(
            QuantityDTO first,
            QuantityDTO second,
            String targetUnit) {

        return response(
                delegate.add(first, second, targetUnit));
    }

    public QuantityMeasurementDTO subtract(
            QuantityDTO first,
            QuantityDTO second,
            String targetUnit) {

        return response(
                delegate.subtract(first, second, targetUnit));
    }

    public QuantityMeasurementDTO divide(
            QuantityDTO first, QuantityDTO second) {

        return response(delegate.divide(first, second));
    }

    public List<QuantityMeasurementDTO> historyByOperation(
            String operation) {

        return repository.getMeasurementsByOperation(operation)
                .stream()
                .map(QuantityMeasurementDTO::fromEntity)
                .toList();
    }

    public List<QuantityMeasurementDTO> historyByType(
            String type) {

        String normalized = type.trim().toUpperCase();

        if (normalized.endsWith("UNIT")) {
            normalized = normalized.substring(
                    0, normalized.length() - 4);
        }

        return repository.getMeasurementsByType(normalized)
                .stream()
                .map(QuantityMeasurementDTO::fromEntity)
                .toList();
    }

    public List<QuantityMeasurementDTO> errorHistory() {

        return repository.getAllMeasurements()
                .stream()
                .filter(QuantityMeasurementEntity::hasError)
                .map(QuantityMeasurementDTO::fromEntity)
                .toList();
    }

    public long countByOperation(String operation) {

        return historyByOperation(operation).size();
    }
}
