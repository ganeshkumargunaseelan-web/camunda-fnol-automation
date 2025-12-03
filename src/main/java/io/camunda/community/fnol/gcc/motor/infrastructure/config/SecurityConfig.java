/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class SecurityConfig {

    private final FnolProperties fnolProperties;

    public SecurityConfig(FnolProperties fnolProperties) {
        this.fnolProperties = fnolProperties;
    }

    /**
     * Cache of rate limit buckets per IP address.
     */
    @Bean
    public Map<String, Bucket> rateLimitBuckets() {
        return new ConcurrentHashMap<>();
    }

    /**
     * Create a new rate limit bucket for an IP address.
     *
     * @return configured Bucket for rate limiting
     */
    @Bean
    public Bucket defaultRateLimitBucket() {
        int requestsPerMinute = fnolProperties.security().rateLimit().requestsPerMinute();

        Bandwidth limit = Bandwidth.builder()
                .capacity(requestsPerMinute)
                .refillGreedy(requestsPerMinute, Duration.ofMinutes(1))
                .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    /**
     * Factory method to create rate limit buckets.
     */
    public Bucket createBucketForIp() {
        int requestsPerMinute = fnolProperties.security().rateLimit().requestsPerMinute();

        Bandwidth limit = Bandwidth.builder()
                .capacity(requestsPerMinute)
                .refillGreedy(requestsPerMinute, Duration.ofMinutes(1))
                .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    /**
     * Check if rate limiting is enabled.
     */
    public boolean isRateLimitEnabled() {
        return fnolProperties.security().rateLimit().enabled();
    }

    /**
     * Check if API key authentication is enabled.
     */
    public boolean isApiKeyEnabled() {
        return fnolProperties.security().apiKey().enabled();
    }

    /**
     * Get the configured API key.
     */
    public String getApiKey() {
        return fnolProperties.security().apiKey().key();
    }

    /**
     * Get the API key header name.
     */
    public String getApiKeyHeaderName() {
        return fnolProperties.security().apiKey().headerName();
    }
}
