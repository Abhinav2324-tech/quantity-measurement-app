
package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.*;
import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;
import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.bridgelabz.quantitymeasurement.model.QuantityModel;
import com.bridgelabz.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.bridgelabz.quantitymeasurement.exception.QuantityMeasurementException;

import java.util.function.Supplier;

/**
 * UC15: Business logic for quantity measurements.
 */
public class QuantityMeasurementServiceImpl
        implements IQuantityMeasurementService {

    private final IQuantityMeasurementRepository repository;

    /**
     * Constructor dependency injection.
     */
    public QuantityMeasurementServiceImpl(
            IQuantityMeasurementRepository repository) {

        if (repository == null) {
            throw new IllegalArgumentException(
                    "Repository cannot be null");
        }

        this.repository = repository;
    }

    /**
     * Resolves the concrete measurement enum from DTO data.
     */
    private IMeasurable resolveUnit(
            String type,
            String unitName) {

        if (type == null || unitName == null) {
            throw new QuantityMeasurementException(
                    "Measurement type and unit are required");
        }

        String category = type.trim().toUpperCase();
        String name = unitName.trim().toUpperCase();

        try {
            return switch (category) {
                case "LENGTH" ->
                        LengthUnit.valueOf(name);

                case "WEIGHT" ->
                        WeightUnit.valueOf(name);

                case "VOLUME" ->
                        VolumeUnit.valueOf(name);

                case "TEMPERATURE" ->
                        TemperatureUnit.valueOf(name);

                default -> throw new QuantityMeasurementException(
                        "Unsupported measurement type: " + type);
            };

        } catch (IllegalArgumentException exception) {
            throw new QuantityMeasurementException(
                    "Invalid unit " + unitName
                            + " for " + type,
                    exception);
        }
    }

    private QuantityModel<IMeasurable> toModel(
            QuantityDTO dto) {

        if (dto == null) {
            throw new QuantityMeasurementException(
                    "QuantityDTO cannot be null");
        }

        IMeasurable unit = resolveUnit(
                dto.getMeasurementType(),
                dto.getUnitName());

        return new QuantityModel<>(
                dto.getValue(), unit);
    }


    private QuantityDTO toDTO(
            Quantity<IMeasurable> quantity) {

        IMeasurable unit = quantity.getUnit();

        String measurementType = unit.getMeasurementType();

        String unitName = ((Enum<?>) unit).name();

        return new QuantityDTO(
                quantity.getValue(),
                measurementType,
                unitName);
    }

    private void validateCompatible(
            QuantityModel<IMeasurable> first,
            QuantityModel<IMeasurable> second) {

        if (first.getUnit().getClass()
                != second.getUnit().getClass()) {

            throw new QuantityMeasurementException(
                    "Different measurement categories cannot be combined");
        }
    }

    /**
     * Executes an operation, records its result, and
     * converts domain errors to standardized error entities.
     */
    private QuantityMeasurementEntity execute(
            String operation,
            QuantityDTO first,
            QuantityDTO second,
            Supplier<QuantityMeasurementEntity> action) {

        QuantityMeasurementEntity result;

        try {
            result = action.get();

        } catch (RuntimeException exception) {
            String message = exception.getMessage();

            result = QuantityMeasurementEntity.error(
                    operation, first, second,
                    message == null
                            ? exception.getClass().getSimpleName()
                            : message);
        }

        repository.save(result);
        return result;
    }

    @Override
    public QuantityMeasurementEntity compare(
            QuantityDTO first,
            QuantityDTO second) {

        return execute("COMPARE", first, second, () -> {

            QuantityModel<IMeasurable> firstModel =
                    toModel(first);

            QuantityModel<IMeasurable> secondModel =
                    toModel(second);

            validateCompatible(firstModel, secondModel);

            boolean equal = firstModel.toQuantity()
                    .equals(secondModel.toQuantity());

            return QuantityMeasurementEntity.comparisonResult(
                    first, second, equal);
        });
    }

    @Override
    public QuantityMeasurementEntity convert(
            QuantityDTO source,
            String targetUnit) {

        return execute("CONVERT", source, null, () -> {

            QuantityModel<IMeasurable> model =
                    toModel(source);

            IMeasurable target = resolveUnit(
                    model.getUnit().getMeasurementType(),
                    targetUnit);

            Quantity<IMeasurable> converted =
                    model.toQuantity().convertTo(target);

            return QuantityMeasurementEntity.quantityResult(
                    "CONVERT", source, null,
                    toDTO(converted));
        });
    }

    @Override
    public QuantityMeasurementEntity add(
            QuantityDTO first,
            QuantityDTO second,
            String targetUnit) {

        return execute("ADD", first, second, () -> {

            QuantityModel<IMeasurable> a = toModel(first);
            QuantityModel<IMeasurable> b = toModel(second);

            validateCompatible(a, b);

            IMeasurable target = targetUnit == null
                    ? a.getUnit()
                    : resolveUnit(
                    a.getUnit().getMeasurementType(),
                    targetUnit);

            Quantity<IMeasurable> result =
                    a.toQuantity().add(b.toQuantity(), target);

            return QuantityMeasurementEntity.quantityResult(
                    "ADD", first, second, toDTO(result));
        });
    }

    @Override
    public QuantityMeasurementEntity subtract(
            QuantityDTO first,
            QuantityDTO second,
            String targetUnit) {

        return execute("SUBTRACT", first, second, () -> {

            QuantityModel<IMeasurable> a = toModel(first);
            QuantityModel<IMeasurable> b = toModel(second);

            validateCompatible(a, b);
            IMeasurable target = targetUnit == null
                    ? a.getUnit()
                    : resolveUnit(
                    a.getUnit().getMeasurementType(),
                    targetUnit);

            Quantity<IMeasurable> result =
                    a.toQuantity().subtract(b.toQuantity(), target);

            return QuantityMeasurementEntity.quantityResult(
                    "SUBTRACT", first, second, toDTO(result));
        });
    }

    @Override
    public QuantityMeasurementEntity divide(
            QuantityDTO first,
            QuantityDTO second) {

        return execute("DIVIDE", first, second, () -> {

            QuantityModel<IMeasurable> a = toModel(first);
            QuantityModel<IMeasurable> b = toModel(second);

            validateCompatible(a, b);

            double ratio = a.toQuantity()
                    .divide(b.toQuantity());

            return QuantityMeasurementEntity.divisionResult(
                    first, second, ratio);
        });
    }
}
