/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.application.port.out;

import io.camunda.community.fnol.gcc.motor.domain.model.MotorFnolCase;

public interface WebhookNotifierPort {

    /**
     * Send a notification when an FNOL is created.
     *
     * @param fnolCase the created FNOL case
     */
    void notifyFnolCreated(MotorFnolCase fnolCase);

    /**
     * Send a notification when an FNOL is updated.
     *
     * @param fnolCase the updated FNOL case
     */
    void notifyFnolUpdated(MotorFnolCase fnolCase);
}
