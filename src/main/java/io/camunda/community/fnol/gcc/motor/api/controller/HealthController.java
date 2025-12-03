/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.api.controller;

import io.camunda.community.fnol.gcc.motor.application.service.GccValidationService;
import io.camunda.community.fnol.gcc.motor.infrastructure.config.FnolProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Info", description = "Application information and health")
public class HealthController {

    private final FnolProperties fnolProperties;
    private final GccValidationService validationService;

    public HealthController(FnolProperties fnolProperties, GccValidationService validationService) {
        this.fnolProperties = fnolProperties;
        this.validationService = validationService;
    }

    @GetMapping("/info")
    @Operation(summary = "Application info", description = "Get application information and configuration")
    public ResponseEntity<Map<String, Object>> getInfo() {
        return ResponseEntity.ok(Map.of(
                "application", fnolProperties.branding().appName(),
                "version", "1.0.0",
                "demoMode", fnolProperties.demoMode(),
                "supportedCountries", validationService.getSupportedCountries(),
                "timestamp", LocalDateTime.now()
        ));
    }

    @GetMapping("/countries")
    @Operation(summary = "Supported countries", description = "Get list of supported GCC countries")
    public ResponseEntity<List<String>> getSupportedCountries() {
        return ResponseEntity.ok(validationService.getSupportedCountries());
    }
}
