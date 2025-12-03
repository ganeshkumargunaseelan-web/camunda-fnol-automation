/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.persistence.repository;

import io.camunda.community.fnol.gcc.motor.infrastructure.persistence.entity.FnolSequenceEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FnolSequenceJpaRepository extends JpaRepository<FnolSequenceEntity, String> {

    /**
     * Find and lock a sequence for update (pessimistic locking).
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM FnolSequenceEntity s WHERE s.sequenceName = :name")
    Optional<FnolSequenceEntity> findBySequenceNameForUpdate(@Param("name") String sequenceName);

    /**
     * Increment the sequence value and return the new value.
     */
    @Modifying
    @Query("UPDATE FnolSequenceEntity s SET s.nextValue = s.nextValue + 1, s.updatedAt = CURRENT_TIMESTAMP WHERE s.sequenceName = :name")
    int incrementSequence(@Param("name") String sequenceName);
}
