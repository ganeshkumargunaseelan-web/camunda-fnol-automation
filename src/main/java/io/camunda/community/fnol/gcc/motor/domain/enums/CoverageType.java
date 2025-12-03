/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.domain.enums;

import java.util.Optional;

public enum CoverageType {

    /**
     * Third Party Liability - covers damages to third parties only.
     */
    TPL("Third Party Liability", "Basic coverage for third party damages"),

    /**
     * Comprehensive - covers own vehicle and third party damages.
     */
    COMPREHENSIVE("Comprehensive", "Full coverage including own vehicle damages");

    private final String displayName;
    private final String description;

    CoverageType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Get the display name for UI presentation.
     *
     * @return display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the description of this coverage type.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Check if this is comprehensive coverage.
     *
     * @return true if comprehensive
     */
    public boolean isComprehensive() {
        return this == COMPREHENSIVE;
    }

    /**
     * Check if this is TPL (third party liability) coverage.
     *
     * @return true if TPL
     */
    public boolean isTpl() {
        return this == TPL;
    }

    /**
     * Find coverage type from string value.
     *
     * @param value the value to parse
     * @return Optional containing coverage type if found
     */
    public static Optional<CoverageType> fromValue(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(valueOf(value.toUpperCase().trim()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
