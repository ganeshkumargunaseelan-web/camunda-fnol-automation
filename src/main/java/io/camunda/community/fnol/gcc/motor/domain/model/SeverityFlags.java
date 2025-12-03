/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.domain.model;

public record SeverityFlags(
        boolean notDrivable,
        boolean potentialInjury,
        boolean highValue
) {

    /**
     * Create default (low severity) flags.
     *
     * @return SeverityFlags with all flags false
     */
    public static SeverityFlags defaults() {
        return new SeverityFlags(false, false, false);
    }

    /**
     * Create severity flags from FNOL data.
     *
     * @param drivable whether the vehicle is drivable
     * @param injuries whether there are injuries reported
     * @return SeverityFlags instance
     */
    public static SeverityFlags from(boolean drivable, boolean injuries) {
        return new SeverityFlags(!drivable, injuries, false);
    }

    /**
     * Calculate severity level based on flags.
     *
     * @return severity level string (HIGH, MEDIUM, LOW)
     */
    public String getSeverityLevel() {
        if (potentialInjury) {
            return "HIGH";
        } else if (notDrivable || highValue) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    /**
     * Get the routing destination based on severity.
     *
     * @return route identifier (complex, standard, fast-track)
     */
    public String getRoute() {
        return switch (getSeverityLevel()) {
            case "HIGH" -> "complex";
            case "MEDIUM" -> "standard";
            default -> "fast-track";
        };
    }

    /**
     * Check if this is a high-priority case.
     *
     * @return true if high priority (injuries involved)
     */
    public boolean isHighPriority() {
        return potentialInjury;
    }

    /**
     * Check if this case requires immediate attention.
     *
     * @return true if any severity flag is set
     */
    public boolean requiresAttention() {
        return notDrivable || potentialInjury || highValue;
    }

    /**
     * Get a summary of active flags for logging/display.
     *
     * @return comma-separated list of active flags
     */
    public String getActiveFlagsSummary() {
        StringBuilder sb = new StringBuilder();
        if (potentialInjury) {
            sb.append("INJURY");
        }
        if (notDrivable) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append("NOT_DRIVABLE");
        }
        if (highValue) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append("HIGH_VALUE");
        }
        return sb.isEmpty() ? "NONE" : sb.toString();
    }

    /**
     * Builder for creating SeverityFlags with fluent API.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for SeverityFlags.
     */
    public static class Builder {
        private boolean notDrivable = false;
        private boolean potentialInjury = false;
        private boolean highValue = false;

        public Builder notDrivable(boolean notDrivable) {
            this.notDrivable = notDrivable;
            return this;
        }

        public Builder potentialInjury(boolean potentialInjury) {
            this.potentialInjury = potentialInjury;
            return this;
        }

        public Builder highValue(boolean highValue) {
            this.highValue = highValue;
            return this;
        }

        public SeverityFlags build() {
            return new SeverityFlags(notDrivable, potentialInjury, highValue);
        }
    }
}
