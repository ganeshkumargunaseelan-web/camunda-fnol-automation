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

public record PlateNumber(
        String value,
        GccCountry country,
        String normalizedValue
) {

    /**
     * Plate number patterns per country.
     * Note: These are simplified patterns; actual validation may need to be more specific.
     */
    private static final Map<GccCountry, PlateRule> PLATE_RULES = Map.of(
            GccCountry.AE, new PlateRule(
                    Pattern.compile("^[A-Z]{1,3}\\s?\\d{1,5}$", Pattern.CASE_INSENSITIVE),
                    "Letters (1-3) + Numbers (1-5), e.g., A 12345"
            ),
            GccCountry.SA, new PlateRule(
                    Pattern.compile("^[A-Z]{3}\\s?\\d{4}$", Pattern.CASE_INSENSITIVE),
                    "3 Letters + 4 Numbers, e.g., ABC 1234"
            ),
            GccCountry.QA, new PlateRule(
                    Pattern.compile("^\\d{1,6}$"),
                    "1-6 digits, e.g., 123456"
            ),
            GccCountry.BH, new PlateRule(
                    Pattern.compile("^\\d{1,6}$"),
                    "1-6 digits, e.g., 12345"
            ),
            GccCountry.KW, new PlateRule(
                    // Allow digits with optional Arabic letters
                    Pattern.compile("^\\d{1,6}(\\s?[\\u0600-\\u06FF]{1,3})?$"),
                    "Numbers + optional Arabic letters, e.g., 12345"
            ),
            GccCountry.OM, new PlateRule(
                    Pattern.compile("^\\d{1,6}\\s?[A-Z]{1,2}$", Pattern.CASE_INSENSITIVE),
                    "Numbers + 1-2 Letters, e.g., 12345 AA"
            )
    );

    /**
     * Compact constructor with validation.
     */
    public PlateNumber {
        Objects.requireNonNull(value, "Plate number cannot be null");
        Objects.requireNonNull(country, "Country cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Plate number cannot be blank");
        }

        // Normalize the value
        normalizedValue = normalize(value);

        // Validate against country rules
        PlateRule rule = PLATE_RULES.get(country);
        if (rule != null && !rule.pattern.matcher(normalizedValue).matches()) {
            throw new IllegalArgumentException(
                    "Invalid " + country.getFullName() + " plate number format: " + value +
                            ". Expected format: " + rule.formatHint
            );
        }
    }

    /**
     * Create a PlateNumber with country context.
     *
     * @param plateNumber the plate number string
     * @param country     the GCC country
     * @return PlateNumber instance
     */
    public static PlateNumber of(String plateNumber, GccCountry country) {
        return new PlateNumber(plateNumber, country, null);
    }

    /**
     * Try to parse a plate number, returning Optional.
     *
     * @param plateNumber the plate number string
     * @param country     the GCC country
     * @return Optional containing the parsed plate, or empty if invalid
     */
    public static Optional<PlateNumber> tryParse(String plateNumber, GccCountry country) {
        try {
            return Optional.of(of(plateNumber, country));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Check if a string is a valid plate number for a specific country.
     *
     * @param plateNumber the plate number to validate
     * @param country     the country
     * @return true if valid
     */
    public static boolean isValid(String plateNumber, GccCountry country) {
        return tryParse(plateNumber, country).isPresent();
    }

    /**
     * Normalize the plate number.
     *
     * @param plate the raw plate number
     * @return normalized plate number
     */
    private static String normalize(String plate) {
        if (plate == null) {
            return null;
        }

        // Trim and convert Latin letters to uppercase
        String normalized = plate.trim();

        // Normalize multiple spaces to single space
        normalized = normalized.replaceAll("\\s+", " ");

        // Convert to uppercase (for Latin letters)
        StringBuilder result = new StringBuilder();
        for (char c : normalized.toCharArray()) {
            if (Character.isLetter(c) && c < 128) {
                result.append(Character.toUpperCase(c));
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    /**
     * Get a display-friendly format of the plate number.
     *
     * @return formatted plate number
     */
    public String getDisplayFormat() {
        return normalizedValue != null ? normalizedValue : value;
    }

    @Override
    public String toString() {
        return normalizedValue != null ? normalizedValue : value;
    }

    /**
     * Internal record for plate number validation rules.
     */
    private record PlateRule(Pattern pattern, String formatHint) {}
}
