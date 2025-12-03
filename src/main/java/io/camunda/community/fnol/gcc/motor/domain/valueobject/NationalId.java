/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.domain.valueobject;

import io.camunda.community.fnol.gcc.motor.domain.enums.GccCountry;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

public record NationalId(
        String value,
        GccCountry country,
        String normalizedValue
) {

    /**
     * National ID patterns per country.
     */
    private static final Map<GccCountry, NationalIdRule> ID_RULES = Map.of(
            GccCountry.AE, new NationalIdRule(
                    "Emirates ID",
                    Pattern.compile("^784-?\\d{4}-?\\d{7}-?\\d$"),
                    "784-YYYY-NNNNNNN-C"
            ),
            GccCountry.SA, new NationalIdRule(
                    "National ID / Iqama",
                    Pattern.compile("^[12]\\d{9}$"),
                    "10 digits starting with 1 or 2"
            ),
            GccCountry.QA, new NationalIdRule(
                    "QID",
                    Pattern.compile("^\\d{11}$"),
                    "11 digits"
            ),
            GccCountry.BH, new NationalIdRule(
                    "CPR",
                    Pattern.compile("^\\d{9}$"),
                    "9 digits"
            ),
            GccCountry.KW, new NationalIdRule(
                    "Civil ID",
                    Pattern.compile("^\\d{12}$"),
                    "12 digits"
            ),
            GccCountry.OM, new NationalIdRule(
                    "Resident Card",
                    Pattern.compile("^\\d{8,9}$"),
                    "8-9 digits"
            )
    );

    /**
     * Compact constructor with validation.
     */
    public NationalId {
        Objects.requireNonNull(value, "National ID cannot be null");
        Objects.requireNonNull(country, "Country cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("National ID cannot be blank");
        }

        // Normalize the value
        normalizedValue = normalize(value, country);

        // Validate against country rules
        NationalIdRule rule = ID_RULES.get(country);
        if (rule != null && !rule.pattern.matcher(normalizedValue).matches()) {
            throw new IllegalArgumentException(
                    "Invalid " + rule.name + " format: " + value +
                            ". Expected format: " + rule.formatHint
            );
        }
    }

    /**
     * Create a NationalId with country context.
     *
     * @param nationalId the ID string
     * @param country    the GCC country
     * @return NationalId instance
     */
    public static NationalId of(String nationalId, GccCountry country) {
        return new NationalId(nationalId, country, null);
    }

    /**
     * Try to parse a national ID, returning Optional.
     *
     * @param nationalId the ID string
     * @param country    the GCC country
     * @return Optional containing the parsed ID, or empty if invalid
     */
    public static Optional<NationalId> tryParse(String nationalId, GccCountry country) {
        try {
            return Optional.of(of(nationalId, country));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Check if a string is a valid national ID for a specific country.
     *
     * @param nationalId the ID to validate
     * @param country    the country
     * @return true if valid
     */
    public static boolean isValid(String nationalId, GccCountry country) {
        return tryParse(nationalId, country).isPresent();
    }

    /**
     * Normalize the national ID based on country-specific rules.
     *
     * @param id      the raw ID
     * @param country the country
     * @return normalized ID
     */
    private static String normalize(String id, GccCountry country) {
        if (id == null) {
            return null;
        }

        String normalized = id.trim();

        // Country-specific normalization
        switch (country) {
            case AE -> {
                // Remove dashes for Emirates ID validation, then re-add for display
                normalized = normalized.replaceAll("-", "");
                if (normalized.length() == 15) {
                    // Format: 784YYYYNNNNNNNC -> 784-YYYY-NNNNNNN-C
                    normalized = normalized.substring(0, 3) + "-" +
                            normalized.substring(3, 7) + "-" +
                            normalized.substring(7, 14) + "-" +
                            normalized.substring(14);
                }
            }
            case SA, QA, BH, KW, OM -> {
                // Remove any spaces or dashes, keep only digits
                normalized = normalized.replaceAll("[^0-9]", "");
            }
        }

        return normalized;
    }

    /**
     * Get the ID type name for this country.
     *
     * @return human-readable ID type name
     */
    public String getIdTypeName() {
        NationalIdRule rule = ID_RULES.get(country);
        return rule != null ? rule.name : "National ID";
    }

    /**
     * Get a masked version for display (privacy protection).
     *
     * @return masked ID (e.g., 784-****-*******-1)
     */
    public String getMaskedValue() {
        if (normalizedValue == null || normalizedValue.length() < 4) {
            return "****";
        }

        return switch (country) {
            case AE -> normalizedValue.substring(0, 4) + "-****-*******-" +
                    normalizedValue.charAt(normalizedValue.length() - 1);
            default -> normalizedValue.substring(0, 2) +
                    "*".repeat(normalizedValue.length() - 4) +
                    normalizedValue.substring(normalizedValue.length() - 2);
        };
    }

    @Override
    public String toString() {
        return normalizedValue != null ? normalizedValue : value;
    }

    /**
     * Internal record for National ID validation rules.
     */
    private record NationalIdRule(String name, Pattern pattern, String formatHint) {}
}
