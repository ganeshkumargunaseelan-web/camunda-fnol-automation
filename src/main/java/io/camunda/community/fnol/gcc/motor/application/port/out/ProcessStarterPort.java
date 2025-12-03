/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.application.port.out;

import io.camunda.community.fnol.gcc.motor.domain.model.MotorFnolCase;

public interface ProcessStarterPort {

    /**
     * Start a new FNOL process instance.
     *
     * @param fnolCase the FNOL case data to pass as process variable
     * @return process instance key
     */
    String startFnolProcess(MotorFnolCase fnolCase);
}
