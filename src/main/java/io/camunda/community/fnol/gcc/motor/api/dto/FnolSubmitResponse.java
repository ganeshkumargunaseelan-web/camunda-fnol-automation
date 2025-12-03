/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FnolSubmitResponse(
        String fnolId,
        String status,
        String severityLevel,
        String route,
        String processInstanceKey,
        LocalDateTime createdAt,
        boolean isDuplicate,
        String message
) {
    /**
     * Create a success response.
     */
    public static FnolSubmitResponse success(String fnolId, String status, String severityLevel,
                                              String route, String processInstanceKey,
                                              LocalDateTime createdAt) {
        return new FnolSubmitResponse(
                fnolId, status, severityLevel, route, processInstanceKey,
                createdAt, false, "FNOL submitted successfully"
        );
    }

    /**
     * Create a duplicate response (idempotency).
     */
    public static FnolSubmitResponse duplicate(String fnolId, String status, String severityLevel,
                                                String route, String processInstanceKey,
                                                LocalDateTime createdAt) {
        return new FnolSubmitResponse(
                fnolId, status, severityLevel, route, processInstanceKey,
                createdAt, true, "FNOL already submitted with this idempotency key"
        );
    }
}
