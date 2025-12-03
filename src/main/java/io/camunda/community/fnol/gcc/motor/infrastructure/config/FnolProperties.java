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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "fnol")
@Validated
public record FnolProperties(
        boolean demoMode,
        @Valid @NotNull BrandingProperties branding,
        @Valid @NotNull SecurityProperties security,
        @Valid @NotNull WebhookProperties webhook,
        @Valid @NotNull IdGenerationProperties idGeneration,
        @Valid @NotNull AttachmentsProperties attachments,
        @Valid @NotNull ValidationProperties validation
) {

    /**
     * Branding configuration for UI customization.
     */
    public record BrandingProperties(
            @NotBlank String appName,
            String logoUrl,
            String faviconUrl,
            @NotBlank String primaryColor,
            @NotBlank String secondaryColor
    ) {
        public BrandingProperties {
            if (appName == null || appName.isBlank()) {
                appName = "GCC Motor FNOL";
            }
            if (primaryColor == null || primaryColor.isBlank()) {
                primaryColor = "#1976d2";
            }
            if (secondaryColor == null || secondaryColor.isBlank()) {
                secondaryColor = "#dc004e";
            }
        }
    }

    /**
     * Security configuration including API key and rate limiting.
     */
    public record SecurityProperties(
            @Valid @NotNull ApiKeyProperties apiKey,
            @Valid @NotNull RateLimitProperties rateLimit,
            @Valid @NotNull CorsProperties cors
    ) {

        public record ApiKeyProperties(
                boolean enabled,
                @NotBlank String headerName,
                String key
        ) {
            public ApiKeyProperties {
                if (headerName == null || headerName.isBlank()) {
                    headerName = "X-API-Key";
                }
            }
        }

        public record RateLimitProperties(
                boolean enabled,
                @Min(1) @Max(1000) int requestsPerMinute
        ) {
            public RateLimitProperties {
                if (requestsPerMinute <= 0) {
                    requestsPerMinute = 30;
                }
            }
        }

        public record CorsProperties(
                @NotBlank String allowedOrigins,
                @NotBlank String allowedMethods
        ) {
            public CorsProperties {
                if (allowedOrigins == null || allowedOrigins.isBlank()) {
                    allowedOrigins = "*";
                }
                if (allowedMethods == null || allowedMethods.isBlank()) {
                    allowedMethods = "GET,POST,OPTIONS";
                }
            }
        }
    }

    /**
     * Webhook notification configuration.
     */
    public record WebhookProperties(
            boolean enabled,
            String url,
            String secret,
            @Min(1000) @Max(30000) int timeoutMs,
            @Min(0) @Max(10) int retryCount
    ) {
        public WebhookProperties {
            if (timeoutMs <= 0) {
                timeoutMs = 5000;
            }
            if (retryCount < 0) {
                retryCount = 3;
            }
        }
    }

    /**
     * FNOL ID generation configuration.
     */
    public record IdGenerationProperties(
            @NotBlank String prefix,
            @Min(4) @Max(10) int sequencePadding
    ) {
        public IdGenerationProperties {
            if (prefix == null || prefix.isBlank()) {
                prefix = "FNOL";
            }
            if (sequencePadding <= 0) {
                sequencePadding = 6;
            }
        }
    }

    /**
     * Attachments configuration.
     */
    public record AttachmentsProperties(
            @Min(1) @Max(50) int maxCount,
            @Min(100) @Max(4096) int maxUrlLength,
            @NotBlank String allowedTypes
    ) {
        public AttachmentsProperties {
            if (maxCount <= 0) {
                maxCount = 10;
            }
            if (maxUrlLength <= 0) {
                maxUrlLength = 2048;
            }
            if (allowedTypes == null || allowedTypes.isBlank()) {
                allowedTypes = "image,video,document,audio";
            }
        }
    }

    /**
     * Validation configuration.
     */
    public record ValidationProperties(
            boolean strictMode
    ) {}
}
