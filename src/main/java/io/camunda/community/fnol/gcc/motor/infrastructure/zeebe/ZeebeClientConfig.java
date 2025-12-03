/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.zeebe;

import io.camunda.community.fnol.gcc.motor.infrastructure.config.CamundaProperties;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.ZeebeClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZeebeClientConfig {

    private static final Logger log = LoggerFactory.getLogger(ZeebeClientConfig.class);

    /**
     * Zeebe client for Camunda Cloud (SaaS).
     */
    @Bean
    @ConditionalOnProperty(name = "camunda.client.mode", havingValue = "cloud")
    public ZeebeClient cloudZeebeClient(CamundaProperties properties) {
        log.info("Configuring Zeebe client for Camunda Cloud");

        var cloud = properties.client().cloud();

        return ZeebeClient.newCloudClientBuilder()
                .withClusterId(cloud.clusterId())
                .withClientId(cloud.clientId())
                .withClientSecret(cloud.clientSecret())
                .withRegion(cloud.region())
                .build();
    }

    /**
     * Zeebe client for Self-Managed deployment.
     */
    @Bean
    @ConditionalOnProperty(name = "camunda.client.mode", havingValue = "self-managed")
    public ZeebeClient selfManagedZeebeClient(CamundaProperties properties) {
        log.info("Configuring Zeebe client for Self-Managed deployment");

        var zeebe = properties.client().zeebe();

        ZeebeClientBuilder builder = ZeebeClient.newClientBuilder()
                .gatewayAddress(zeebe.gatewayAddress());

        if (zeebe.security() != null && zeebe.security().plaintext()) {
            builder.usePlaintext();
        }

        return builder.build();
    }

    /**
     * No-op Zeebe client for demo mode.
     * Returns null - demo mode uses DemoProcessStarter instead.
     */
    @Bean
    @ConditionalOnProperty(name = "camunda.client.mode", havingValue = "demo", matchIfMissing = true)
    public ZeebeClient demoZeebeClient() {
        log.info("Demo mode - Zeebe client not configured");
        return null;
    }
}
