/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        String correlationId,
        List<FieldError> fieldErrors
) {
    /**
     * Field-level validation error.
     */
    public record FieldError(
            String field,
            String code,
            String message
    ) {}

    /**
     * Create a simple error response.
     */
    public static ErrorResponse of(int status, String error, String message, String path, String correlationId) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status,
                error,
                message,
                path,
                correlationId,
                null
        );
    }

    /**
     * Create a validation error response with field errors.
     */
    public static ErrorResponse validation(String message, String path, String correlationId, List<FieldError> fieldErrors) {
        return new ErrorResponse(
                LocalDateTime.now(),
                400,
                "Validation Error",
                message,
                path,
                correlationId,
                fieldErrors
        );
    }
}
