/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "camunda")
@Validated
public record CamundaProperties(
        @Valid @NotNull ClientProperties client,
        @Valid @NotNull ProcessProperties process
) {

    /**
     * Client configuration for connecting to Camunda 8.
     */
    public record ClientProperties(
            @NotBlank String mode,
            @Valid CloudProperties cloud,
            @Valid ZeebeProperties zeebe
    ) {
        public ClientProperties {
            if (mode == null || mode.isBlank()) {
                mode = "self-managed";
            }
        }

        /**
         * Check if running in SaaS mode.
         */
        public boolean isSaasMode() {
            return "saas".equalsIgnoreCase(mode);
        }

        /**
         * Check if running in Self-Managed mode.
         */
        public boolean isSelfManagedMode() {
            return "self-managed".equalsIgnoreCase(mode);
        }

        /**
         * Check if running in Demo mode (mock Camunda).
         */
        public boolean isDemoMode() {
            return "demo".equalsIgnoreCase(mode);
        }

        /**
         * Camunda Cloud (SaaS) configuration.
         */
        public record CloudProperties(
                String region,
                String clusterId,
                String clientId,
                String clientSecret
        ) {
            /**
             * Check if SaaS configuration is complete.
             */
            public boolean isConfigured() {
                return region != null && !region.isBlank()
                        && clusterId != null && !clusterId.isBlank()
                        && clientId != null && !clientId.isBlank()
                        && clientSecret != null && !clientSecret.isBlank();
            }
        }

        /**
         * Zeebe (Self-Managed) configuration.
         */
        public record ZeebeProperties(
                String gatewayAddress,
                @Valid SecurityProperties security
        ) {
            public ZeebeProperties {
                if (gatewayAddress == null || gatewayAddress.isBlank()) {
                    gatewayAddress = "localhost:26500";
                }
            }

            public record SecurityProperties(
                    boolean plaintext
            ) {}
        }
    }

    /**
     * Process configuration for BPMN process management.
     */
    public record ProcessProperties(
            @NotBlank String id,
            boolean autoDeploy
    ) {
        public ProcessProperties {
            if (id == null || id.isBlank()) {
                id = "MOTOR_FNOL_PROCESS";
            }
        }
    }
}
