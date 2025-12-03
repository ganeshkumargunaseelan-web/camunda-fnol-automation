/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.api.exception;

import io.camunda.community.fnol.gcc.motor.api.dto.ErrorResponse;
import io.camunda.community.fnol.gcc.motor.application.exception.DuplicateSubmissionException;
import io.camunda.community.fnol.gcc.motor.application.exception.FnolValidationException;
import io.camunda.community.fnol.gcc.motor.application.exception.ProcessStartException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle validation exceptions from @Valid annotations.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        BindingResult bindingResult = ex.getBindingResult();

        List<ErrorResponse.FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
                .map(error -> new ErrorResponse.FieldError(
                        error.getField(),
                        error.getCode(),
                        error.getDefaultMessage()
                ))
                .toList();

        log.warn("Validation failed for request to {}: {} errors",
                request.getRequestURI(), fieldErrors.size());

        ErrorResponse response = ErrorResponse.validation(
                "Validation failed",
                request.getRequestURI(),
                getCorrelationId(),
                fieldErrors
        );

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handle FNOL validation exceptions.
     */
    @ExceptionHandler(FnolValidationException.class)
    public ResponseEntity<ErrorResponse> handleFnolValidationException(
            FnolValidationException ex,
            HttpServletRequest request) {

        log.warn("FNOL validation failed: {}", ex.getMessage());

        List<ErrorResponse.FieldError> fieldErrors = ex.getErrors().stream()
                .map(error -> new ErrorResponse.FieldError(
                        error.field(),
                        error.code(),
                        error.message()
                ))
                .toList();

        ErrorResponse response = ErrorResponse.validation(
                ex.getMessage(),
                request.getRequestURI(),
                getCorrelationId(),
                fieldErrors
        );

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handle duplicate submission exceptions (should not normally reach here as
     * they're handled in the service layer, but just in case).
     */
    @ExceptionHandler(DuplicateSubmissionException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateSubmissionException(
            DuplicateSubmissionException ex,
            HttpServletRequest request) {

        log.info("Duplicate submission: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.of(
                HttpStatus.CONFLICT.value(),
                "Duplicate Submission",
                ex.getMessage(),
                request.getRequestURI(),
                getCorrelationId()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Handle process start exceptions.
     */
    @ExceptionHandler(ProcessStartException.class)
    public ResponseEntity<ErrorResponse> handleProcessStartException(
            ProcessStartException ex,
            HttpServletRequest request) {

        log.error("Process start failed for FNOL {}: {}", ex.getFnolId(), ex.getMessage());

        ErrorResponse response = ErrorResponse.of(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Process Start Failed",
                "Unable to start workflow process. Please try again later.",
                request.getRequestURI(),
                getCorrelationId()
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    /**
     * Handle JSON parsing errors.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        log.warn("Invalid JSON in request: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Request",
                "Unable to parse request body. Please check JSON format.",
                request.getRequestURI(),
                getCorrelationId()
        );

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handle illegal argument exceptions.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        log.warn("Invalid argument: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Argument",
                ex.getMessage(),
                request.getRequestURI(),
                getCorrelationId()
        );

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handle all other exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error processing request to {}", request.getRequestURI(), ex);

        ErrorResponse response = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                request.getRequestURI(),
                getCorrelationId()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Get correlation ID from MDC or return null.
     */
    private String getCorrelationId() {
        return MDC.get("correlationId");
    }
}
