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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record GccMobileNumber(
        String value,
        GccCountry country
) {

    /**
     * MSISDN validation rules per country.
     * Format: prefix -> (total length, local number pattern)
     */
    private static final Map<GccCountry, MsisdnRule> MSISDN_RULES = Map.of(
            GccCountry.AE, new MsisdnRule("+971", 12, Pattern.compile("^\\+971[0-9]{9}$")),
            GccCountry.SA, new MsisdnRule("+966", 13, Pattern.compile("^\\+966[0-9]{9}$")),
            GccCountry.QA, new MsisdnRule("+974", 12, Pattern.compile("^\\+974[0-9]{8}$")),
            GccCountry.BH, new MsisdnRule("+973", 12, Pattern.compile("^\\+973[0-9]{8}$")),
            GccCountry.KW, new MsisdnRule("+965", 12, Pattern.compile("^\\+965[0-9]{8}$")),
            GccCountry.OM, new MsisdnRule("+968", 12, Pattern.compile("^\\+968[0-9]{8}$"))
    );

    /**
     * Generic pattern for initial validation.
     */
    private static final Pattern GENERIC_MSISDN_PATTERN = Pattern.compile("^\\+[0-9]{10,15}$");

    /**
     * Compact constructor with validation.
     */
    public GccMobileNumber {
        Objects.requireNonNull(value, "Mobile number cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Mobile number cannot be blank");
        }

        // Normalize the number
        value = normalize(value);

        // Store final value for lambda reference
        final String normalizedValue = value;

        // Validate generic format
        if (!GENERIC_MSISDN_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Invalid mobile number format: " + value + ". Expected format: +XXXXXXXXXXXX"
            );
        }

        // Detect country if not provided
        if (country == null) {
            country = detectCountry(value).orElseThrow(() ->
                    new IllegalArgumentException("Could not detect country from mobile number: " + normalizedValue)
            );
        }

        // Validate against country-specific rules
        MsisdnRule rule = MSISDN_RULES.get(country);
        if (rule != null && !rule.pattern.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Invalid " + country.getFullName() + " mobile number format: " + value +
                            ". Expected " + rule.length + " digits starting with " + rule.prefix
            );
        }
    }

    /**
     * Create a GccMobileNumber from a string, auto-detecting the country.
     *
     * @param mobileNumber the mobile number string
     * @return GccMobileNumber instance
     */
    public static GccMobileNumber of(String mobileNumber) {
        return new GccMobileNumber(mobileNumber, null);
    }

    /**
     * Create a GccMobileNumber with explicit country.
     *
     * @param mobileNumber the mobile number string
     * @param country      the GCC country
     * @return GccMobileNumber instance
     */
    public static GccMobileNumber of(String mobileNumber, GccCountry country) {
        return new GccMobileNumber(mobileNumber, country);
    }

    /**
     * Try to parse a mobile number, returning Optional.
     *
     * @param mobileNumber the mobile number string
     * @return Optional containing the parsed number, or empty if invalid
     */
    public static Optional<GccMobileNumber> tryParse(String mobileNumber) {
        try {
            return Optional.of(of(mobileNumber));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Check if a string is a valid GCC mobile number.
     *
     * @param mobileNumber the number to validate
     * @return true if valid
     */
    public static boolean isValid(String mobileNumber) {
        return tryParse(mobileNumber).isPresent();
    }

    /**
     * Check if a string is a valid mobile number for a specific country.
     *
     * @param mobileNumber the number to validate
     * @param country      the expected country
     * @return true if valid for that country
     */
    public static boolean isValid(String mobileNumber, GccCountry country) {
        try {
            GccMobileNumber parsed = of(mobileNumber, country);
            return parsed.country() == country;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Normalize a mobile number (remove spaces, dashes, etc.).
     *
     * @param number the raw number
     * @return normalized number
     */
    private static String normalize(String number) {
        if (number == null) {
            return null;
        }
        // Remove common separators and whitespace
        String normalized = number.replaceAll("[\\s\\-().]+", "");

        // Ensure it starts with +
        if (!normalized.startsWith("+") && normalized.startsWith("00")) {
            normalized = "+" + normalized.substring(2);
        }

        return normalized;
    }

    /**
     * Detect the GCC country from a mobile number based on prefix.
     *
     * @param number the normalized number
     * @return Optional containing detected country
     */
    private static Optional<GccCountry> detectCountry(String number) {
        if (number == null || !number.startsWith("+")) {
            return Optional.empty();
        }

        for (Map.Entry<GccCountry, MsisdnRule> entry : MSISDN_RULES.entrySet()) {
            if (number.startsWith(entry.getValue().prefix)) {
                return Optional.of(entry.getKey());
            }
        }

        return Optional.empty();
    }

    /**
     * Get the local number (without country code).
     *
     * @return local number portion
     */
    public String getLocalNumber() {
        MsisdnRule rule = MSISDN_RULES.get(country);
        if (rule != null && value.startsWith(rule.prefix)) {
            return value.substring(rule.prefix.length());
        }
        return value;
    }

    /**
     * Get the display format with spaces.
     *
     * @return formatted number (e.g., +971 50 123 4567)
     */
    public String getDisplayFormat() {
        String local = getLocalNumber();
        if (local.length() >= 9) {
            return country.getPhonePrefix() + " " +
                    local.substring(0, 2) + " " +
                    local.substring(2, 5) + " " +
                    local.substring(5);
        }
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Internal record for MSISDN validation rules.
     */
    private record MsisdnRule(String prefix, int length, Pattern pattern) {}
}
