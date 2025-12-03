/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.application.service;

import io.camunda.community.fnol.gcc.motor.application.exception.DuplicateSubmissionException;
import io.camunda.community.fnol.gcc.motor.application.port.out.IdempotencyPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Optional;

@Service
public class IdempotencyService {

    private static final Logger log = LoggerFactory.getLogger(IdempotencyService.class);
    private static final Duration DEFAULT_TTL = Duration.ofHours(24);

    private final IdempotencyPort idempotencyPort;

    public IdempotencyService(IdempotencyPort idempotencyPort) {
        this.idempotencyPort = idempotencyPort;
    }

    /**
     * Check if an idempotency key exists and register it if not.
     * If the key already exists, throws DuplicateSubmissionException with the existing FNOL ID.
     *
     * @param idempotencyKey the idempotency key from the client
     * @throws DuplicateSubmissionException if the key already exists
     */
    public void checkAndRegister(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            log.debug("No idempotency key provided, skipping idempotency check");
            return;
        }

        String hashedKey = hashKey(idempotencyKey);
        Optional<String> existingFnolId = idempotencyPort.findFnolIdByKey(hashedKey);

        if (existingFnolId.isPresent()) {
            log.info("Duplicate submission detected for idempotency key (hash: {}), existing FNOL: {}",
                    hashedKey.substring(0, 8) + "...", existingFnolId.get());
            throw new DuplicateSubmissionException(idempotencyKey, existingFnolId.get());
        }

        log.debug("Idempotency key registered (hash: {}...)", hashedKey.substring(0, 8));
    }

    /**
     * Register an idempotency key with its associated FNOL ID.
     * Call this after successfully creating an FNOL case.
     *
     * @param idempotencyKey the idempotency key
     * @param fnolId         the generated FNOL ID
     */
    public void register(String idempotencyKey, String fnolId) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            return;
        }

        String hashedKey = hashKey(idempotencyKey);
        Instant expiresAt = Instant.now().plus(DEFAULT_TTL);

        idempotencyPort.save(hashedKey, fnolId, expiresAt);
        log.debug("Idempotency key saved for FNOL {}", fnolId);
    }

    /**
     * Check if an idempotency key exists and return the associated FNOL ID if it does.
     *
     * @param idempotencyKey the idempotency key
     * @return Optional containing the FNOL ID if the key exists
     */
    public Optional<String> findExisting(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            return Optional.empty();
        }

        String hashedKey = hashKey(idempotencyKey);
        return idempotencyPort.findFnolIdByKey(hashedKey);
    }

    /**
     * Generate an idempotency key from submission data.
     * This can be used when the client doesn't provide their own key.
     *
     * @param mobileNumber the mobile number
     * @param nationalId   the national ID
     * @param plateNumber  the plate number
     * @param incidentDate the incident date as string
     * @return generated idempotency key
     */
    public String generateKey(String mobileNumber, String nationalId,
                              String plateNumber, String incidentDate) {
        String data = String.join("|",
                normalize(mobileNumber),
                normalize(nationalId),
                normalize(plateNumber),
                normalize(incidentDate)
        );
        return hashKey(data);
    }

    /**
     * Clean up expired idempotency records.
     * This should be called periodically (e.g., via scheduled task).
     *
     * @return number of records deleted
     */
    public int cleanupExpired() {
        int deleted = idempotencyPort.deleteExpired(Instant.now());
        if (deleted > 0) {
            log.info("Cleaned up {} expired idempotency records", deleted);
        }
        return deleted;
    }

    /**
     * Hash an idempotency key using SHA-256.
     * We store hashed keys to:
     * 1. Ensure consistent key length in database
     * 2. Protect potentially sensitive data in the key
     *
     * @param key the key to hash
     * @return hashed key as hex string
     */
    private String hashKey(String key) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(key.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is always available in Java
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    /**
     * Normalize a value for consistent key generation.
     *
     * @param value the value to normalize
     * @return normalized value
     */
    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase().replaceAll("\\s+", "");
    }
}
