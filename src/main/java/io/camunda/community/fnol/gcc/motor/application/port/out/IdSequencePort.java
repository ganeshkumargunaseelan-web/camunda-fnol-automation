/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.application.port.out;

public interface IdSequencePort {

    /**
     * Get the next sequence number using the default sequence.
     *
     * @return next available sequence number
     */
    long nextValue();

    /**
     * Get the next sequence number for a named sequence.
     *
     * @param sequenceName the sequence name
     * @return next available sequence number
     */
    long nextValue(String sequenceName);
}
