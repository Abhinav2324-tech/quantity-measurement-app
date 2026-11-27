
package com.bridgelabz.quantitymeasurement.controller;

import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.bridgelabz.quantitymeasurement.exception.GlobalExceptionHandler;
import com.bridgelabz.quantitymeasurement.service.SpringQuantityMeasurementService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuantityRestController.class)
@Import(GlobalExceptionHandler.class)
class QuantityRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SpringQuantityMeasurementService service;

    private static final String INPUT = """
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
    void testCompareEndpoint() throws Exception {

        when(service.compare(any(), any()))
                .thenReturn(new QuantityMeasurementDTO(
                        "COMPARE",
                        new QuantityDTO(1, "LENGTH", "FEET"),
                        new QuantityDTO(12, "LENGTH", "INCHES"),
                        null, true, null, false, null));

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType("application/json")
                        .content(INPUT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comparisonResult")
                        .value(true));

        verify(service).compare(any(), any());
    }

    @Test
    void testAddEndpoint() throws Exception {

        when(service.add(any(), any(), nullable(String.class)))
                .thenReturn(new QuantityMeasurementDTO(
                        "ADD",
                        new QuantityDTO(1, "LENGTH", "FEET"),
                        new QuantityDTO(12, "LENGTH", "INCHES"),
                        new QuantityDTO(2, "LENGTH", "FEET"),
                        null, null, false, null));

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType("application/json")
                        .content(INPUT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.value")
                        .value(2.0));
    }

    @Test
    void testInvalidRequestReturns400() throws Exception {

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHistoryEndpoint() throws Exception {

        when(service.historyByOperation("ADD"))
                .thenReturn(List.of());

        mockMvc.perform(get(
                        "/api/v1/quantities/history/operation/ADD"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testOperationCount() throws Exception {

        when(service.countByOperation("ADD"))
                .thenReturn(3L);

        mockMvc.perform(get(
                        "/api/v1/quantities/count/ADD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(3));
    }
}
