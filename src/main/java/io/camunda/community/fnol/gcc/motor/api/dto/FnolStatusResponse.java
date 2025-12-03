/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.api.dto;

import java.time.LocalDateTime;

public record FnolStatusResponse(
        String fnolId,
        String status,
        String severityLevel,
        String route,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
