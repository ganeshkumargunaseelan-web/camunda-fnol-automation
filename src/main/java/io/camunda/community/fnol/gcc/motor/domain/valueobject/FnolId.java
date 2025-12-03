/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.domain.valueobject;

import io.camunda.community.fnol.gcc.motor.domain.enums.GccCountry;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record FnolId(
        String prefix,
        GccCountry country,
        int year,
        long sequence
) {

    /**
     * Pattern for parsing FNOL IDs.
     */
    private static final Pattern FNOL_ID_PATTERN = Pattern.compile(
            "^([A-Z]+)-([A-Z]{2})-(\\d{4})-(\\d+)$"
    );

    /**
     * Compact constructor with validation.
     */
    public FnolId {
        Objects.requireNonNull(prefix, "Prefix cannot be null");
        Objects.requireNonNull(country, "Country cannot be null");
        if (prefix.isBlank()) {
            throw new IllegalArgumentException("Prefix cannot be blank");
        }
        if (year < 2020 || year > 2100) {
            throw new IllegalArgumentException("Year must be between 2020 and 2100");
        }
        if (sequence < 1) {
            throw new IllegalArgumentException("Sequence must be positive");
        }
    }

    /**
     * Get the formatted FNOL ID string.
     *
     * @return formatted ID (e.g., FNOL-AE-2025-000001)
     */
    public String getValue() {
        return String.format("%s-%s-%d-%06d", prefix, country.name(), year, sequence);
    }

    /**
     * Get the formatted FNOL ID string with custom padding.
     *
     * @param padding number of digits for sequence padding
     * @return formatted ID
     */
    public String getValue(int padding) {
        String format = "%s-%s-%d-%0" + padding + "d";
        return String.format(format, prefix, country.name(), year, sequence);
    }

    /**
     * Parse an FNOL ID string into an FnolId object.
     *
     * @param fnolIdString the string to parse (e.g., "FNOL-AE-2025-000001")
     * @return parsed FnolId
     * @throws IllegalArgumentException if the format is invalid
     */
    public static FnolId parse(String fnolIdString) {
        if (fnolIdString == null || fnolIdString.isBlank()) {
            throw new IllegalArgumentException("FNOL ID cannot be null or blank");
        }

        Matcher matcher = FNOL_ID_PATTERN.matcher(fnolIdString.trim().toUpperCase());
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Invalid FNOL ID format. Expected: PREFIX-CC-YYYY-NNNNNN, got: " + fnolIdString
            );
        }

        String prefix = matcher.group(1);
        String countryCode = matcher.group(2);
        int year = Integer.parseInt(matcher.group(3));
        long sequence = Long.parseLong(matcher.group(4));

        GccCountry country = GccCountry.fromCode(countryCode)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid country code in FNOL ID: " + countryCode
                ));

        return new FnolId(prefix, country, year, sequence);
    }

    /**
     * Check if a string is a valid FNOL ID format.
     *
     * @param fnolIdString the string to check
     * @return true if valid format
     */
    public static boolean isValid(String fnolIdString) {
        if (fnolIdString == null || fnolIdString.isBlank()) {
            return false;
        }
        Matcher matcher = FNOL_ID_PATTERN.matcher(fnolIdString.trim().toUpperCase());
        if (!matcher.matches()) {
            return false;
        }
        // Also verify country code is valid GCC country
        String countryCode = matcher.group(2);
        return GccCountry.isValidGccCountry(countryCode);
    }

    @Override
    public String toString() {
        return getValue();
    }
}
