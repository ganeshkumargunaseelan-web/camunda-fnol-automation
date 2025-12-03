/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@ConfigurationProperties(prefix = "gcc")
@Validated
public record GccCountryProperties(
        @NotNull Map<String, CountryConfig> countries
) {

    /**
     * Get configuration for a specific country code.
     *
     * @param countryCode ISO 2-letter country code (AE, SA, QA, BH, KW, OM)
     * @return Optional containing country config if found
     */
    public Optional<CountryConfig> getCountry(String countryCode) {
        if (countryCode == null || countries == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(countries.get(countryCode.toUpperCase()));
    }

    /**
     * Check if a country code is supported.
     *
     * @param countryCode ISO 2-letter country code
     * @return true if country is configured
     */
    public boolean isSupported(String countryCode) {
        return getCountry(countryCode).isPresent();
    }

    /**
     * Configuration for a single GCC country.
     */
    public record CountryConfig(
            @NotBlank String name,
            @NotBlank String timezone,
            @NotBlank String msisdnPrefix,
            int msisdnLength,
            @NotBlank String nationalIdPattern,
            @NotBlank String nationalIdName,
            @NotBlank String platePattern
    ) {
        // Cached compiled patterns
        private static final Map<String, Pattern> PATTERN_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

        /**
         * Get compiled regex pattern for National ID validation.
         */
        public Pattern getNationalIdRegex() {
            return PATTERN_CACHE.computeIfAbsent(
                    "nid:" + nationalIdPattern,
                    k -> Pattern.compile(nationalIdPattern)
            );
        }

        /**
         * Get compiled regex pattern for Plate Number validation.
         */
        public Pattern getPlateRegex() {
            return PATTERN_CACHE.computeIfAbsent(
                    "plate:" + platePattern,
                    k -> Pattern.compile(platePattern)
            );
        }

        /**
         * Validate a mobile number against this country's rules.
         *
         * @param mobileNumber the mobile number to validate
         * @return true if valid
         */
        public boolean isValidMsisdn(String mobileNumber) {
            if (mobileNumber == null || mobileNumber.isBlank()) {
                return false;
            }
            String cleaned = mobileNumber.replaceAll("[\\s\\-()]", "");
            return cleaned.startsWith(msisdnPrefix) && cleaned.length() == msisdnLength;
        }

        /**
         * Validate a National ID against this country's pattern.
         *
         * @param nationalId the national ID to validate
         * @return true if valid
         */
        public boolean isValidNationalId(String nationalId) {
            if (nationalId == null || nationalId.isBlank()) {
                return false;
            }
            return getNationalIdRegex().matcher(nationalId.trim()).matches();
        }

        /**
         * Validate a plate number against this country's pattern.
         *
         * @param plateNumber the plate number to validate
         * @return true if valid
         */
        public boolean isValidPlateNumber(String plateNumber) {
            if (plateNumber == null || plateNumber.isBlank()) {
                return false;
            }
            return getPlateRegex().matcher(plateNumber.trim().toUpperCase()).matches();
        }
    }
}
