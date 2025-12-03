/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.domain.service;

import io.camunda.community.fnol.gcc.motor.domain.enums.CoverageType;
import io.camunda.community.fnol.gcc.motor.domain.model.MotorFnolCase;
import io.camunda.community.fnol.gcc.motor.domain.model.SeverityFlags;

/**
 * Domain service for calculating FNOL severity and routing.
 * Pure domain logic - no framework dependencies.
 */
public class SeverityCalculator {

    /**
     * Calculate severity flags for an FNOL case.
     *
     * @param fnolCase the FNOL case
     * @return calculated severity flags
     */
    public SeverityFlags calculate(MotorFnolCase fnolCase) {
        return SeverityFlags.builder()
                .notDrivable(!fnolCase.isDrivable())
                .potentialInjury(fnolCase.hasInjuries())
                .highValue(isHighValue(fnolCase))
                .build();
    }

    /**
     * Calculate severity level string.
     *
     * @param fnolCase the FNOL case
     * @return severity level (HIGH, MEDIUM, LOW)
     */
    public String calculateLevel(MotorFnolCase fnolCase) {
        return calculate(fnolCase).getSeverityLevel();
    }

    /**
     * Calculate routing destination.
     *
     * @param fnolCase the FNOL case
     * @return route (complex, standard, fast-track)
     */
    public String calculateRoute(MotorFnolCase fnolCase) {
        return calculate(fnolCase).getRoute();
    }

    /**
     * Determine routing based on case characteristics.
     * This implements the DMN decision table logic:
     * <ul>
     *   <li>Injuries → complex (HIGH severity)</li>
     *   <li>Not drivable → standard (MEDIUM severity)</li>
     *   <li>Fleet + Comprehensive + Drivable + No injuries → fast-track</li>
     *   <li>TPL + Drivable + No injuries → fast-track</li>
     *   <li>Comprehensive + Drivable + No injuries → standard</li>
     * </ul>
     *
     * @param injuries     whether injuries are reported
     * @param drivable     whether vehicle is drivable
     * @param coverageType the coverage type
     * @param isFleet      whether this is a fleet vehicle
     * @return route (complex, standard, fast-track)
     */
    public String determineRoute(boolean injuries, boolean drivable,
                                  CoverageType coverageType, boolean isFleet) {

        // Rule 1: Any injuries → complex review
        if (injuries) {
            return "complex";
        }

        // Rule 2: Not drivable → standard review
        if (!drivable) {
            return "standard";
        }

        // Rule 3: Fleet + Comprehensive + Drivable + No injuries → fast-track
        if (isFleet && coverageType == CoverageType.COMPREHENSIVE) {
            return "fast-track";
        }

        // Rule 4: TPL + Drivable + No injuries → fast-track
        if (coverageType == CoverageType.TPL) {
            return "fast-track";
        }

        // Rule 5: Comprehensive + Drivable + No injuries → standard
        // (non-fleet comprehensive gets human review)
        if (coverageType == CoverageType.COMPREHENSIVE) {
            return "standard";
        }

        // Default: standard review
        return "standard";
    }

    /**
     * Determine if a case is high value based on coverage and other factors.
     * This is a simplified check - in production, you might check estimated repair costs,
     * vehicle value, or other factors.
     *
     * @param fnolCase the FNOL case
     * @return true if high value
     */
    private boolean isHighValue(MotorFnolCase fnolCase) {
        // For now, comprehensive non-fleet vehicles are considered potentially high value
        // This could be extended to check vehicle make/model, estimated damage, etc.
        return fnolCase.getCoverageType() == CoverageType.COMPREHENSIVE
                && !fnolCase.isFleetFlag()
                && !fnolCase.isDrivable();
    }

    /**
     * Get a human-readable severity description.
     *
     * @param severityLevel the severity level
     * @return description
     */
    public String getSeverityDescription(String severityLevel) {
        return switch (severityLevel) {
            case "HIGH" -> "High priority - requires immediate attention (potential injury)";
            case "MEDIUM" -> "Medium priority - requires review (vehicle not drivable or high value)";
            case "LOW" -> "Low priority - can be processed via fast-track";
            default -> "Unknown severity";
        };
    }

    /**
     * Get a human-readable route description.
     *
     * @param route the route
     * @return description
     */
    public String getRouteDescription(String route) {
        return switch (route) {
            case "complex" -> "Complex case - assigned to senior claims handler";
            case "standard" -> "Standard review - assigned to claims team";
            case "fast-track" -> "Fast-track processing - automated or minimal review";
            default -> "Unknown route";
        };
    }
}
