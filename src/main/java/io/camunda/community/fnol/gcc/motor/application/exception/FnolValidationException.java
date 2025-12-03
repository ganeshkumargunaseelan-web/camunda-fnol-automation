/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.application.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FnolValidationException extends RuntimeException {

    private final List<ValidationError> errors;

    public FnolValidationException(String message) {
        super(message);
        this.errors = new ArrayList<>();
    }

    public FnolValidationException(String message, List<ValidationError> errors) {
        super(message);
        this.errors = errors != null ? new ArrayList<>(errors) : new ArrayList<>();
    }

    public FnolValidationException(ValidationError error) {
        super(error.message());
        this.errors = List.of(error);
    }

    /**
     * Get validation errors.
     *
     * @return unmodifiable list of errors
     */
    public List<ValidationError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    /**
     * Check if there are any errors.
     *
     * @return true if errors exist
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * Get the number of errors.
     *
     * @return error count
     */
    public int getErrorCount() {
        return errors.size();
    }

    /**
     * Builder for creating validation exception with multiple errors.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Create exception for a single field error.
     */
    public static FnolValidationException forField(String field, String code, String message) {
        return new FnolValidationException(new ValidationError(field, code, message));
    }

    /**
     * Validation error details.
     */
    public record ValidationError(
            String field,
            String code,
            String message
    ) {
        public static ValidationError of(String field, String code, String message) {
            return new ValidationError(field, code, message);
        }
    }

    /**
     * Builder for FnolValidationException.
     */
    public static class Builder {
        private final List<ValidationError> errors = new ArrayList<>();

        public Builder addError(String field, String code, String message) {
            errors.add(new ValidationError(field, code, message));
            return this;
        }

        public Builder addError(ValidationError error) {
            errors.add(error);
            return this;
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }

        public FnolValidationException build() {
            if (errors.isEmpty()) {
                return new FnolValidationException("Validation failed");
            }
            String message = errors.size() == 1
                    ? errors.get(0).message()
                    : "Validation failed with " + errors.size() + " errors";
            return new FnolValidationException(message, errors);
        }

        public void throwIfErrors() {
            if (hasErrors()) {
                throw build();
            }
        }
    }
}
