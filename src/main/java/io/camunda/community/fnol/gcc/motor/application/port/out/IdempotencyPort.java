/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.application.port.out;

import java.time.Instant;
import java.util.Optional;

public interface IdempotencyPort {

    /**
     * Find the FNOL ID associated with an idempotency key.
     *
     * @param hashedKey the hashed idempotency key
     * @return Optional containing the FNOL ID if found
     */
    Optional<String> findFnolIdByKey(String hashedKey);

    /**
     * Save an idempotency key with its associated FNOL ID.
     *
     * @param hashedKey the hashed idempotency key
     * @param fnolId    the FNOL ID
     * @param expiresAt when the key should expire
     */
    void save(String hashedKey, String fnolId, Instant expiresAt);

    /**
     * Delete expired idempotency records.
     *
     * @param before delete records that expired before this instant
     * @return number of records deleted
     */
    int deleteExpired(Instant before);
}
