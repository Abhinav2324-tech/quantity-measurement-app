
package com.bridgelabz.quantitymeasurement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quantity_measurements")
public class QuantityJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String operation;

    private Double firstValue;
    private String firstUnit;
    private String firstMeasurementType;

    private Double secondValue;
    private String secondUnit;
    private String secondMeasurementType;

    private Double resultValue;
    private String resultUnit;
    private String resultMeasurementType;

    private Boolean comparisonResult;
    private Double divisionResult;

    private boolean error;

    @Column(length = 1000)
    private String errorMessage;

    private LocalDateTime createdAt;

    public QuantityJpaEntity() {
    }

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static QuantityJpaEntity from(
            QuantityMeasurementEntity source) {

        QuantityJpaEntity entity = new QuantityJpaEntity();

        entity.operation = source.getOperation();
        entity.error = source.hasError();
        entity.errorMessage = source.getErrorMessage();

        if (source.getFirstOperand() != null) {
            entity.firstValue =
                    source.getFirstOperand().getValue();
            entity.firstUnit =
                    source.getFirstOperand().getUnitName();
            entity.firstMeasurementType =
                    source.getFirstOperand().getMeasurementType();
        }

        if (source.getSecondOperand() != null) {
            entity.secondValue =
                    source.getSecondOperand().getValue();
            entity.secondUnit =
                    source.getSecondOperand().getUnitName();
            entity.secondMeasurementType =
                    source.getSecondOperand().getMeasurementType();
        }

        if (source.getResult() != null) {
            entity.resultValue =
                    source.getResult().getValue();
            entity.resultUnit =
                    source.getResult().getUnitName();
            entity.resultMeasurementType =
                    source.getResult().getMeasurementType();
        }

        entity.comparisonResult = source.getComparisonResult();
        entity.divisionResult = source.getDivisionResult();

        return entity;
    }

    public Long getId() {
        return id;
    }

    public String getOperation() {
        return operation;
    }

    public String getFirstMeasurementType() {
        return firstMeasurementType;
    }

    public boolean isError() {
        return error;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public QuantityMeasurementEntity toDomainEntity() {

        com.bridgelabz.quantitymeasurement.dto.QuantityDTO first =
                firstValue == null ? null
                        : new com.bridgelabz.quantitymeasurement.dto.QuantityDTO(
                        firstValue,
                        firstMeasurementType,
                        firstUnit);

        com.bridgelabz.quantitymeasurement.dto.QuantityDTO second =
                secondValue == null ? null
                        : new com.bridgelabz.quantitymeasurement.dto.QuantityDTO(
                        secondValue,
                        secondMeasurementType,
                        secondUnit);

        if (error) {
            return QuantityMeasurementEntity.error(
                    operation, first, second, errorMessage);
        }

        if ("COMPARE".equals(operation)) {
            return QuantityMeasurementEntity.comparisonResult(
                    first, second, comparisonResult);
        }

        if ("DIVIDE".equals(operation)) {
            return QuantityMeasurementEntity.divisionResult(
                    first, second, divisionResult);
        }

        com.bridgelabz.quantitymeasurement.dto.QuantityDTO result =
                resultValue == null ? null
                        : new com.bridgelabz.quantitymeasurement.dto.QuantityDTO(
                        resultValue,
                        resultMeasurementType,
                        resultUnit);

        return QuantityMeasurementEntity.quantityResult(
                operation, first, second, result);
    }
}
