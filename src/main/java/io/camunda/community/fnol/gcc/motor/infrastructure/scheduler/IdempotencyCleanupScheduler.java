/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.scheduler;

import io.camunda.community.fnol.gcc.motor.application.service.IdempotencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class IdempotencyCleanupScheduler {

    private static final Logger log = LoggerFactory.getLogger(IdempotencyCleanupScheduler.class);

    private final IdempotencyService idempotencyService;

    public IdempotencyCleanupScheduler(IdempotencyService idempotencyService) {
        this.idempotencyService = idempotencyService;
    }

    /**
     * Clean up expired idempotency keys every hour.
     */
    @Scheduled(fixedRate = 3600000) // 1 hour in milliseconds
    public void cleanupExpiredKeys() {
        log.debug("Starting idempotency key cleanup");
        try {
            int deleted = idempotencyService.cleanupExpired();
            if (deleted > 0) {
                log.info("Idempotency cleanup completed: {} records deleted", deleted);
            }
        } catch (Exception e) {
            log.error("Error during idempotency cleanup", e);
        }
    }
}
