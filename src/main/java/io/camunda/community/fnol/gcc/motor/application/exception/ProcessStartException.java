/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.application.exception;

public class ProcessStartException extends RuntimeException {

    private final String fnolId;
    private final String processId;

    public ProcessStartException(String fnolId, String message) {
        super("Failed to start process for FNOL " + fnolId + ": " + message);
        this.fnolId = fnolId;
        this.processId = null;
    }

    public ProcessStartException(String fnolId, String message, Throwable cause) {
        super("Failed to start process for FNOL " + fnolId + ": " + message, cause);
        this.fnolId = fnolId;
        this.processId = null;
    }

    public ProcessStartException(String fnolId, String processId, String message) {
        super("Failed to start process " + processId + " for FNOL " + fnolId + ": " + message);
        this.fnolId = fnolId;
        this.processId = processId;
    }

    public ProcessStartException(String fnolId, String processId, String message, Throwable cause) {
        super("Failed to start process " + processId + " for FNOL " + fnolId + ": " + message, cause);
        this.fnolId = fnolId;
        this.processId = processId;
    }

    /**
     * Get the FNOL ID that failed to start a process.
     *
     * @return FNOL ID
     */
    public String getFnolId() {
        return fnolId;
    }

    /**
     * Get the process ID that was attempted.
     *
     * @return process ID
     */
    public String getProcessId() {
        return processId;
    }
}
