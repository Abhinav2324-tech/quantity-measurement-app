
package com.bridgelabz.quantitymeasurement.controller;

import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityInputDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.bridgelabz.quantitymeasurement.service.SpringQuantityMeasurementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/quantities")
@Tag(
        name = "Quantity Measurements",
        description = "REST API for quantity operations"
)
public class QuantityRestController {

    private final SpringQuantityMeasurementService service;

    public QuantityRestController(
            SpringQuantityMeasurementService service) {
        this.service = service;
    }

    private QuantityDTO first(QuantityInputDTO request) {
        return request.thisQuantityDTO().toDTO();
    }

    private QuantityDTO second(QuantityInputDTO request) {

        if (request.thatQuantityDTO() == null) {
            throw new IllegalArgumentException(
                    "Second quantity is required");
        }

        return request.thatQuantityDTO().toDTO();
    }

    @PostMapping("/compare")
    @Operation(summary = "Compare two quantities")
    public QuantityMeasurementDTO compare(
            @Valid @RequestBody QuantityInputDTO request) {

        return service.compare(
                first(request), second(request));
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert a quantity")
    public QuantityMeasurementDTO convert(
            @Valid @RequestBody QuantityInputDTO request) {

        String target = request.targetUnit();

        // Also allow the UC17 reference format where
        // the second quantity specifies the target unit.
        if (target == null && request.thatQuantityDTO() != null) {
            target = request.thatQuantityDTO().unit();
        }

        if (target == null || target.isBlank()) {
            throw new IllegalArgumentException(
                    "Target unit is required");
        }

        return service.convert(first(request), target);
    }

    @PostMapping("/add")
    @Operation(summary = "Add two quantities")
    public QuantityMeasurementDTO add(
            @Valid @RequestBody QuantityInputDTO request) {

        return service.add(
                first(request),
                second(request),
                request.targetUnit());
    }

    @PostMapping("/subtract")
    @Operation(summary = "Subtract two quantities")
    public QuantityMeasurementDTO subtract(
            @Valid @RequestBody QuantityInputDTO request) {

        return service.subtract(
                first(request),
                second(request),
                request.targetUnit());
    }

    @PostMapping("/divide")
    @Operation(summary = "Divide two quantities")
    public QuantityMeasurementDTO divide(
            @Valid @RequestBody QuantityInputDTO request) {

        return service.divide(
                first(request), second(request));
    }

    @GetMapping("/history/operation/{operation}")
    @Operation(summary = "Get history by operation")
    public List<QuantityMeasurementDTO> historyByOperation(
            @PathVariable String operation) {

        return service.historyByOperation(operation);
    }

    @GetMapping("/history/type/{type}")
    @Operation(summary = "Get history by measurement type")
    public List<QuantityMeasurementDTO> historyByType(
            @PathVariable String type) {

        return service.historyByType(type);
    }

    @GetMapping("/history/errored")
    @Operation(summary = "Get failed measurement operations")
    public List<QuantityMeasurementDTO> errorHistory() {
        return service.errorHistory();
    }

    @GetMapping("/count/{operation}")
    @Operation(summary = "Count operations")
    public Map<String, Object> count(
            @PathVariable String operation) {

        return Map.of(
                "operation", operation.toUpperCase(),
                "count", service.countByOperation(operation));
    }
}
