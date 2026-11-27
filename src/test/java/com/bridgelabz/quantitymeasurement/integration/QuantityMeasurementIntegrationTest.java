
package com.bridgelabz.quantitymeasurement.integration;

import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;
import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.bridgelabz.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.bridgelabz.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.bridgelabz.quantitymeasurement.util.ConnectionPool;
import com.bridgelabz.quantitymeasurement.controller.QuantityMeasurementController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementIntegrationTest {

    private ConnectionPool pool;
    private QuantityMeasurementDatabaseRepository repository;
    private QuantityMeasurementController controller;

    @BeforeEach
    void setUp() {

        String url = "jdbc:h2:mem:integration_"
                + UUID.randomUUID().toString().replace("-", "")
                + ";DB_CLOSE_DELAY=-1";

        pool = new ConnectionPool(
                url, "sa", "", 3, 1, 10000);

        repository =
                new QuantityMeasurementDatabaseRepository(pool);

        var service =
                new QuantityMeasurementServiceImpl(repository);

        controller = new QuantityMeasurementController(service);
    }

    @AfterEach
    void tearDown() {
        repository.releaseResources();
    }

    private QuantityDTO length(double value, String unit) {
        return new QuantityDTO(value, "LENGTH", unit);
    }

    @Test
    void testEndToEndAdditionPersistence() {

        QuantityMeasurementEntity result =
                controller.performAddition(
                        length(10.0, "FEET"),
                        length(12.0, "INCHES"),
                        "FEET");

        assertFalse(result.hasError());
        assertEquals(11.0, result.getResult().getValue());

        assertEquals(1, repository.getTotalCount());

        QuantityMeasurementEntity stored =
                repository.getAllMeasurements().get(0);

        assertEquals(11.0,
                stored.getResult().getValue());
    }

    @Test
    void testEndToEndConversionPersistence() {

        QuantityMeasurementEntity result =
                controller.performConversion(
                        length(2.0, "FEET"),
                        "INCHES");

        assertFalse(result.hasError());
        assertEquals(24.0,
                result.getResult().getValue());

        assertEquals(1,
                repository.getMeasurementsByOperation("CONVERT").size());
    }

    @Test
    void testDivisionByZeroPersistedAsError() {

        QuantityMeasurementEntity result =
                controller.performDivision(
                        length(10.0, "FEET"),
                        length(0.0, "FEET"));

        assertTrue(result.hasError());
        assertEquals(1, repository.getTotalCount());

        QuantityMeasurementEntity stored =
                repository.getAllMeasurements().get(0);

        assertTrue(stored.hasError());
    }

    @Test
    void testMultipleOperationsPersisted() {

        controller.performComparison(
                length(1.0, "FEET"),
                length(12.0, "INCHES"));

        controller.performAddition(
                length(1.0, "FEET"),
                length(1.0, "FEET"),
                "FEET");

        controller.performSubtraction(
                length(10.0, "FEET"),
                length(5.0, "FEET"),
                "FEET");

        assertEquals(3, repository.getTotalCount());
        assertEquals(3,
                repository.getAllMeasurements().size());
    }
}
