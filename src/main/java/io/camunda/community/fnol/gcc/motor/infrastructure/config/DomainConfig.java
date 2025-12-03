/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.config;

import io.camunda.community.fnol.gcc.motor.domain.service.SeverityCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for domain services.
 * Domain services are pure POJOs registered as Spring beans here
 * to maintain hexagonal architecture purity.
 */
@Configuration
public class DomainConfig {

    @Bean
    public SeverityCalculator severityCalculator() {
        return new SeverityCalculator();
    }
}
