/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.application.exception;

public class DuplicateSubmissionException extends RuntimeException {

    private final String idempotencyKey;
    private final String existingFnolId;

    public DuplicateSubmissionException(String idempotencyKey, String existingFnolId) {
        super("Duplicate submission detected for idempotency key: " + idempotencyKey +
                ". Existing FNOL ID: " + existingFnolId);
        this.idempotencyKey = idempotencyKey;
        this.existingFnolId = existingFnolId;
    }

    /**
     * Get the idempotency key that caused the duplicate detection.
     *
     * @return idempotency key
     */
    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    /**
     * Get the existing FNOL ID for this idempotency key.
     *
     * @return existing FNOL ID
     */
    public String getExistingFnolId() {
        return existingFnolId;
    }
}
