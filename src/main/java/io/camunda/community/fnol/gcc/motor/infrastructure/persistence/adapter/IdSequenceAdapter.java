/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.persistence.adapter;

import io.camunda.community.fnol.gcc.motor.application.port.out.IdSequencePort;
import io.camunda.community.fnol.gcc.motor.infrastructure.persistence.entity.FnolSequenceEntity;
import io.camunda.community.fnol.gcc.motor.infrastructure.persistence.repository.FnolSequenceJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class IdSequenceAdapter implements IdSequencePort {

    private static final String DEFAULT_SEQUENCE_NAME = "FNOL";

    private final FnolSequenceJpaRepository jpaRepository;

    public IdSequenceAdapter(FnolSequenceJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public long nextValue() {
        return nextValue(DEFAULT_SEQUENCE_NAME);
    }

    @Override
    @Transactional
    public long nextValue(String sequenceName) {
        FnolSequenceEntity sequence = jpaRepository.findBySequenceNameForUpdate(sequenceName)
                .orElseGet(() -> createSequence(sequenceName));

        long value = sequence.getNextValue();
        sequence.setNextValue(value + 1);
        jpaRepository.save(sequence);

        return value;
    }

    /**
     * Create a new sequence if it doesn't exist.
     */
    private FnolSequenceEntity createSequence(String sequenceName) {
        FnolSequenceEntity sequence = new FnolSequenceEntity();
        sequence.setSequenceName(sequenceName);
        sequence.setNextValue(1L);
        return jpaRepository.save(sequence);
    }
}
