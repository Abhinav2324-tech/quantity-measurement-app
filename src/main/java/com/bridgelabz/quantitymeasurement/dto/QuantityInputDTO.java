
package com.bridgelabz.quantitymeasurement.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuantityInputDTO(

        @NotNull @Valid
        QuantityPayload thisQuantityDTO,

        @Valid
        QuantityPayload thatQuantityDTO,

        String targetUnit
) {

    public record QuantityPayload(
            @NotNull Double value,
            @NotBlank String unit,
            @NotBlank String measurementType
    ) {

        public QuantityDTO toDTO() {

            String category = measurementType.trim().toUpperCase();

            // Accept names from the UC17 reference:
            // LengthUnit, WeightUnit, VolumeUnit, TemperatureUnit.
            if (category.endsWith("UNIT")) {
                category = category.substring(
                        0, category.length() - 4);
            }

            return new QuantityDTO(
                    value, category, unit);
        }
    }
}
