/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.application.port.out;

import io.camunda.community.fnol.gcc.motor.domain.model.MotorFnolCase;

import java.util.Optional;

public interface FnolRepositoryPort {

    /**
     * Save an FNOL case to the repository.
     *
     * @param fnolCase the case to save
     * @return saved case with any generated fields populated
     */
    MotorFnolCase save(MotorFnolCase fnolCase);

    /**
     * Find an FNOL case by its ID.
     *
     * @param fnolId the FNOL ID
     * @return Optional containing the case if found
     */
    Optional<MotorFnolCase> findByFnolId(String fnolId);

    /**
     * Check if an FNOL exists with the given ID.
     *
     * @param fnolId the FNOL ID
     * @return true if exists
     */
    boolean existsByFnolId(String fnolId);

    /**
     * Update the process instance key for an FNOL.
     *
     * @param fnolId            the FNOL ID
     * @param processInstanceKey the Zeebe process instance key
     */
    void updateProcessInstanceKey(String fnolId, String processInstanceKey);

    /**
     * Update the status of an FNOL.
     *
     * @param fnolId the FNOL ID
     * @param status the new status
     */
    void updateStatus(String fnolId, String status);
}
