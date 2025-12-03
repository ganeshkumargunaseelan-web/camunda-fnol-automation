/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.persistence.repository;

import io.camunda.community.fnol.gcc.motor.infrastructure.persistence.entity.IdempotencyKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface IdempotencyKeyJpaRepository extends JpaRepository<IdempotencyKeyEntity, Long> {

    /**
     * Find an idempotency key by its hashed value.
     */
    Optional<IdempotencyKeyEntity> findByHashedKey(String hashedKey);

    /**
     * Delete expired idempotency keys.
     */
    @Modifying
    @Query("DELETE FROM IdempotencyKeyEntity i WHERE i.expiresAt < :before")
    int deleteByExpiresAtBefore(@Param("before") Instant before);
}
