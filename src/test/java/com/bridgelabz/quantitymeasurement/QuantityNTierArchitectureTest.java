
package com.bridgelabz.quantitymeasurement;

import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;
import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.bridgelabz.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.bridgelabz.quantitymeasurement.service.IQuantityMeasurementService;
import com.bridgelabz.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.bridgelabz.quantitymeasurement.controller.QuantityMeasurementController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuantityNTierArchitectureTest {

    private IQuantityMeasurementService service;
    private QuantityMeasurementController controller;
    private TestRepository repository;

    /**
     * Test-only repository to avoid disk operations.
     */
    private static class TestRepository
            implements IQuantityMeasurementRepository {

        private final List<QuantityMeasurementEntity> records =
                new ArrayList<>();

        @Override
        public void save(QuantityMeasurementEntity entity) {
            records.add(entity);
        }

        @Override
        public List<QuantityMeasurementEntity> getAllMeasurements() {
            return new ArrayList<>(records);
        }
    }

    @BeforeEach
    void setUp() {
        repository = new TestRepository();
        service = new QuantityMeasurementServiceImpl(repository);
        controller = new QuantityMeasurementController(service);
    }

    private QuantityDTO length(double value, String unit) {
        return new QuantityDTO(value, "LENGTH", unit);
    }

    private QuantityDTO weight(double value, String unit) {
        return new QuantityDTO(value, "WEIGHT", unit);
    }

    private QuantityDTO volume(double value, String unit) {
        return new QuantityDTO(value, "VOLUME", unit);
    }

    private QuantityDTO temperature(double value, String unit) {
        return new QuantityDTO(value, "TEMPERATURE", unit);
    }

    @Test
    void testDTOStoresMeasurementData() {
        QuantityDTO dto = length(10.0, "FEET");

        assertEquals(10.0, dto.getValue());
        assertEquals("LENGTH", dto.getMeasurementType());
        assertEquals("FEET", dto.getUnitName());
    }

    @Test
    void testDTONullUnitRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityDTO(
                        10.0, "LENGTH", null));
    }

    @Test
    void testServiceSameUnitEquality() {
        QuantityMeasurementEntity result =
                service.compare(
                        length(10.0, "FEET"),
                        length(10.0, "FEET"));

        assertFalse(result.hasError());
        assertEquals(Boolean.TRUE,
                result.getComparisonResult());
    }

    @Test
    void testServiceCrossUnitEquality() {
        QuantityMeasurementEntity result =
                service.compare(
                        length(1.0, "FEET"),
                        length(12.0, "INCHES"));

        assertEquals(Boolean.TRUE,
                result.getComparisonResult());
    }

    @Test
    void testServiceConversion() {
        QuantityMeasurementEntity result =
                service.convert(
                        length(1.0, "FEET"),
                        "INCHES");

        assertFalse(result.hasError());
        assertEquals(12.0,
                result.getResult().getValue());
        assertEquals("INCHES",
                result.getResult().getUnitName());
    }

    @Test
    void testServiceAddition() {
        QuantityMeasurementEntity result =
                service.add(
                        length(1.0, "FEET"),
                        length(12.0, "INCHES"),
                        "FEET");

        assertFalse(result.hasError());
        assertEquals(2.0,
                result.getResult().getValue());
    }

    @Test
    void testServiceSubtraction() {
        QuantityMeasurementEntity result =
                service.subtract(
                        length(10.0, "FEET"),
                        length(6.0, "INCHES"),
                        "INCHES");

        assertFalse(result.hasError());
        assertEquals(114.0,
                result.getResult().getValue());
    }

    @Test
    void testServiceDivision() {
        QuantityMeasurementEntity result =
                service.divide(
                        length(24.0, "INCHES"),
                        length(2.0, "FEET"));

        assertFalse(result.hasError());
        assertEquals(1.0,
                result.getDivisionResult(), 1e-9);
    }

    @Test
    void testServiceWeightOperation() {
        QuantityMeasurementEntity result =
                service.add(
                        weight(1.0, "KILOGRAM"),
                        weight(500.0, "GRAM"),
                        "KILOGRAM");

        assertEquals(1.5,
                result.getResult().getValue());
    }

    @Test
    void testServiceVolumeOperation() {
        QuantityMeasurementEntity result =
                service.add(
                        volume(1.0, "LITRE"),
                        volume(1000.0, "MILLILITRE"),
                        "LITRE");

        assertEquals(2.0,
                result.getResult().getValue());
    }

    @Test
    void testServiceTemperatureEquality() {
        QuantityMeasurementEntity result =
                service.compare(
                        temperature(0.0, "CELSIUS"),
                        temperature(32.0, "FAHRENHEIT"));

        assertFalse(result.hasError());
        assertEquals(Boolean.TRUE,
                result.getComparisonResult());
    }

    @Test
    void testTemperatureAdditionProducesErrorEntity() {
        QuantityMeasurementEntity result =
                service.add(
                        temperature(100.0, "CELSIUS"),
                        temperature(50.0, "CELSIUS"),
                        "CELSIUS");

        assertTrue(result.hasError());
        assertNotNull(result.getErrorMessage());
    }

    @Test
    void testDivisionByZeroProducesErrorEntity() {
        QuantityMeasurementEntity result =
                service.divide(
                        length(10.0, "FEET"),
                        length(0.0, "FEET"));

        assertTrue(result.hasError());
        assertTrue(result.getErrorMessage()
                .contains("zero"));
    }

    @Test
    void testCrossCategoryOperationRejected() {
        QuantityMeasurementEntity result =
                service.add(
                        length(10.0, "FEET"),
                        weight(10.0, "KILOGRAM"),
                        "FEET");

        assertTrue(result.hasError());
    }

    @Test
    void testInvalidUnitProducesErrorEntity() {
        QuantityMeasurementEntity result =
                service.convert(
                        length(10.0, "FEET"),
                        "INVALID_UNIT");

        assertTrue(result.hasError());
    }

    @Test
    void testNullDTOProducesErrorEntity() {
        QuantityMeasurementEntity result =
                service.convert(null, "INCHES");

        assertTrue(result.hasError());
    }

    @Test
    void testRepositorySavesSuccessfulOperation() {
        service.add(
                length(1.0, "FEET"),
                length(1.0, "FEET"),
                "FEET");

        assertEquals(1,
                repository.getAllMeasurements().size());
    }

    @Test
    void testRepositorySavesFailedOperation() {
        service.divide(
                length(1.0, "FEET"),
                length(0.0, "FEET"));

        assertEquals(1,
                repository.getAllMeasurements().size());

        assertTrue(repository.getAllMeasurements()
                .get(0).hasError());
    }

    @Test
    void testControllerDelegatesAddition() {
        QuantityMeasurementEntity result =
                controller.performAddition(
                        length(1.0, "FEET"),
                        length(12.0, "INCHES"),
                        "FEET");

        assertEquals(2.0,
                result.getResult().getValue());
    }

    @Test
    void testControllerDelegatesConversion() {
        QuantityMeasurementEntity result =
                controller.performConversion(
                        length(2.0, "FEET"),
                        "INCHES");

        assertEquals(24.0,
                result.getResult().getValue());
    }

    @Test
    void testNullServiceRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityMeasurementController(null));
    }

    @Test
    void testNullRepositoryRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityMeasurementServiceImpl(null));
    }

    @Test
    void testEntityStoresOperationType() {
        QuantityMeasurementEntity result =
                service.add(
                        length(1.0, "FEET"),
                        length(1.0, "FEET"),
                        "FEET");

        assertEquals("ADD", result.getOperation());
    }

    @Test
    void testEndToEndControllerServiceRepository() {
        QuantityMeasurementEntity result =
                controller.performSubtraction(
                        volume(5.0, "LITRE"),
                        volume(500.0, "MILLILITRE"),
                        "LITRE");

        assertFalse(result.hasError());
        assertEquals(4.5,
                result.getResult().getValue());
        assertEquals(1,
                repository.getAllMeasurements().size());
    }
}
