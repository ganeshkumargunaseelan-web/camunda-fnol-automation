/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.domain.enums;

import java.util.Optional;

public enum VehicleType {

    /**
     * Private vehicle - personal use.
     */
    PRIVATE("Private", "Personal use vehicle"),

    /**
     * Commercial vehicle - business use.
     */
    COMMERCIAL("Commercial", "Business/commercial use vehicle");

    private final String displayName;
    private final String description;

    VehicleType(String displayName, String description) {
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
     * Get the description of this vehicle type.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Check if this is a commercial vehicle.
     *
     * @return true if commercial
     */
    public boolean isCommercial() {
        return this == COMMERCIAL;
    }

    /**
     * Check if this is a private vehicle.
     *
     * @return true if private
     */
    public boolean isPrivate() {
        return this == PRIVATE;
    }

    /**
     * Find vehicle type from string value.
     *
     * @param value the value to parse
     * @return Optional containing vehicle type if found
     */
    public static Optional<VehicleType> fromValue(String value) {
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
