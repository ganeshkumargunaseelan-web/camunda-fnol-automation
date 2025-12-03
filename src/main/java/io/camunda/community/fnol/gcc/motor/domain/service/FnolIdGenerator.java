/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.domain.service;

import io.camunda.community.fnol.gcc.motor.domain.valueobject.FnolId;

public interface FnolIdGenerator {

    /**
     * Generate a new unique FNOL ID for the given country.
     * Format: {PREFIX}-{COUNTRY}-{DATE}-{SEQUENCE}
     * Example: FNOL-AE-20250115-000001
     *
     * @param countryCode the country code
     * @param sequence    the sequence number
     * @return generated FNOL ID
     */
    FnolId generate(String countryCode, long sequence);
}
