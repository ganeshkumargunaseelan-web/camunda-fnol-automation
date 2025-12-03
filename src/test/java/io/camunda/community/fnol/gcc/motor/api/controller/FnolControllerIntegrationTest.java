/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.api.controller;

import io.camunda.community.fnol.gcc.motor.api.dto.FnolSubmitRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for FNOL Controller.
 *
 * Note: These tests require proper Zeebe/Camunda 8 test infrastructure.
 * They are disabled by default for CI/CD builds without Camunda.
 * Enable by removing @Disabled when Camunda 8 is properly configured.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("demo")
@Disabled("Requires Camunda 8 infrastructure - enable manually for integration testing")
class FnolControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should submit FNOL successfully")
    void shouldSubmitFnolSuccessfully() throws Exception {
        FnolSubmitRequest request = createValidRequest();
        String idempotencyKey = UUID.randomUUID().toString();

        mockMvc.perform(post("/api/v1/fnol")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Idempotency-Key", idempotencyKey)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fnolId").exists())
                .andExpect(jsonPath("$.status").value("SUBMITTED"))
                .andExpect(jsonPath("$.severityLevel").exists())
                .andExpect(jsonPath("$.route").exists())
                .andExpect(jsonPath("$.isDuplicate").value(false));
    }

    @Test
    @DisplayName("Should return 400 for missing required fields")
    void shouldReturn400ForMissingFields() throws Exception {
        FnolSubmitRequest request = new FnolSubmitRequest(
                null, // missing countryCode
                null, // missing mobileNumber
                null, // missing nationalId
                null, null,
                null, // missing plateNumber
                null, null, null, null, null, null, null, null, false,
                null, // missing incidentDate
                null, null, null, null, null, false, false, false, null, null, null
        );

        mockMvc.perform(post("/api/v1/fnol")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }

    @Test
    @DisplayName("Should return duplicate for same idempotency key")
    void shouldReturnDuplicateForSameIdempotencyKey() throws Exception {
        FnolSubmitRequest request = createValidRequest();
        String idempotencyKey = UUID.randomUUID().toString();

        // First request
        mockMvc.perform(post("/api/v1/fnol")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Idempotency-Key", idempotencyKey)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Second request with same key
        mockMvc.perform(post("/api/v1/fnol")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Idempotency-Key", idempotencyKey)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDuplicate").value(true));
    }

    @Test
    @DisplayName("Should return 404 for non-existent FNOL")
    void shouldReturn404ForNonExistentFnol() throws Exception {
        mockMvc.perform(get("/api/v1/fnol/NON-EXISTENT-ID"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should get supported countries")
    void shouldGetSupportedCountries() throws Exception {
        mockMvc.perform(get("/api/v1/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @DisplayName("Should get app info")
    void shouldGetAppInfo() throws Exception {
        mockMvc.perform(get("/api/v1/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.application").exists())
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.demoMode").value(true));
    }

    private FnolSubmitRequest createValidRequest() {
        return new FnolSubmitRequest(
                "AE",
                "+971501234567",
                "784-1234-5678901-1",
                "Test User",
                "test@example.com",
                "A12345",
                "AE",
                "PRIVATE",
                "Toyota",
                "Camry",
                2022,
                "White",
                "POL-123456",
                "COMPREHENSIVE",
                false,
                "2025-01-15",
                "14:30",
                "Dubai Marina",
                25.0657,
                55.1403,
                "Minor accident at parking",
                true,
                false,
                false,
                null,
                "EN",
                List.of()
        );
    }
}
