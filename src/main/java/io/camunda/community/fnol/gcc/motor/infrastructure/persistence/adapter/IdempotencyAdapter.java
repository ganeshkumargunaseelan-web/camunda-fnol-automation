/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.persistence.adapter;

import io.camunda.community.fnol.gcc.motor.application.port.out.IdempotencyPort;
import io.camunda.community.fnol.gcc.motor.infrastructure.persistence.entity.IdempotencyKeyEntity;
import io.camunda.community.fnol.gcc.motor.infrastructure.persistence.repository.IdempotencyKeyJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Component
public class IdempotencyAdapter implements IdempotencyPort {

    private final IdempotencyKeyJpaRepository jpaRepository;

    public IdempotencyAdapter(IdempotencyKeyJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<String> findFnolIdByKey(String hashedKey) {
        return jpaRepository.findByHashedKey(hashedKey)
                .filter(entity -> entity.getExpiresAt().isAfter(Instant.now()))
                .map(IdempotencyKeyEntity::getFnolId);
    }

    @Override
    @Transactional
    public void save(String hashedKey, String fnolId, Instant expiresAt) {
        IdempotencyKeyEntity entity = new IdempotencyKeyEntity();
        entity.setHashedKey(hashedKey);
        entity.setFnolId(fnolId);
        entity.setExpiresAt(expiresAt);
        jpaRepository.save(entity);
    }

    @Override
    @Transactional
    public int deleteExpired(Instant before) {
        return jpaRepository.deleteByExpiresAtBefore(before);
    }
}
