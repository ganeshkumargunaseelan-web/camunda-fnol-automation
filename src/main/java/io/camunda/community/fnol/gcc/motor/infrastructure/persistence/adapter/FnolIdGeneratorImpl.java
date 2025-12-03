/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.persistence.adapter;

import io.camunda.community.fnol.gcc.motor.domain.enums.GccCountry;
import io.camunda.community.fnol.gcc.motor.domain.service.FnolIdGenerator;
import io.camunda.community.fnol.gcc.motor.domain.valueobject.FnolId;
import io.camunda.community.fnol.gcc.motor.infrastructure.config.FnolProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class FnolIdGeneratorImpl implements FnolIdGenerator {

    private final FnolProperties fnolProperties;

    public FnolIdGeneratorImpl(FnolProperties fnolProperties) {
        this.fnolProperties = fnolProperties;
    }

    @Override
    public FnolId generate(String countryCode, long sequence) {
        String prefix = fnolProperties.idGeneration().prefix();
        int year = LocalDate.now().getYear();

        GccCountry country = GccCountry.fromCode(countryCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid country code: " + countryCode));

        return new FnolId(prefix, country, year, sequence);
    }
}
