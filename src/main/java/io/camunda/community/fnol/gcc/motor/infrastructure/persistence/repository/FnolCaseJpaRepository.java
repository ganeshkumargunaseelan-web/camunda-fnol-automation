/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.persistence.repository;

import io.camunda.community.fnol.gcc.motor.infrastructure.persistence.entity.FnolCaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FnolCaseJpaRepository extends JpaRepository<FnolCaseEntity, Long> {

    /**
     * Find a case by its FNOL ID.
     */
    Optional<FnolCaseEntity> findByFnolId(String fnolId);

    /**
     * Check if a case exists by FNOL ID.
     */
    boolean existsByFnolId(String fnolId);

    /**
     * Update the process instance key for a case.
     */
    @Modifying
    @Query("UPDATE FnolCaseEntity f SET f.processInstanceKey = :processKey, f.updatedAt = CURRENT_TIMESTAMP WHERE f.fnolId = :fnolId")
    int updateProcessInstanceKey(@Param("fnolId") String fnolId, @Param("processKey") String processKey);

    /**
     * Update the status for a case.
     */
    @Modifying
    @Query("UPDATE FnolCaseEntity f SET f.status = :status, f.updatedAt = CURRENT_TIMESTAMP WHERE f.fnolId = :fnolId")
    int updateStatus(@Param("fnolId") String fnolId, @Param("status") String status);
}
