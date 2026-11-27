
package com.bridgelabz.quantitymeasurement;

import com.bridgelabz.quantitymeasurement.repository.QuantityMeasurementRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuantityMeasurementApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuantityMeasurementRepository repository;

    private static final String ADD_REQUEST = """
            {
              "thisQuantityDTO": {
                "value": 1.0,
                "unit": "FEET",
                "measurementType": "LengthUnit"
              },
              "thatQuantityDTO": {
                "value": 12.0,
                "unit": "INCHES",
                "measurementType": "LengthUnit"
              }
            }
            """;

    @Test
    void testSpringBootContextLoads() {
        assertNotNull(repository);
    }

    @Test
    void testEndToEndAdditionAndPersistence() throws Exception {

        long before = repository.count();

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ADD_REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.value")
                        .value(2.0));

        assertEquals(before + 1, repository.count());
    }

    @Test
    void testInvalidRequest() throws Exception {

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHistoryEndpoint() throws Exception {

        mockMvc.perform(get(
                        "/api/v1/quantities/history/operation/ADD"))
                .andExpect(status().isOk());
    }
}
