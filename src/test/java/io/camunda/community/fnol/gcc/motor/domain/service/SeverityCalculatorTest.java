/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.domain.service;

import io.camunda.community.fnol.gcc.motor.domain.enums.CoverageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SeverityCalculatorTest {

    private SeverityCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new SeverityCalculator();
    }

    @Nested
    @DisplayName("Route Determination Tests")
    class RouteDeterminationTests {

        @Test
        @DisplayName("Should route to complex when injuries reported")
        void shouldRouteToComplexWhenInjuries() {
            String route = calculator.determineRoute(true, true, CoverageType.COMPREHENSIVE, false);
            assertThat(route).isEqualTo("complex");
        }

        @Test
        @DisplayName("Should route to complex when injuries even if drivable")
        void shouldRouteToComplexWhenInjuriesEvenIfDrivable() {
            String route = calculator.determineRoute(true, true, CoverageType.TPL, true);
            assertThat(route).isEqualTo("complex");
        }

        @Test
        @DisplayName("Should route to standard when not drivable and no injuries")
        void shouldRouteToStandardWhenNotDrivable() {
            String route = calculator.determineRoute(false, false, CoverageType.COMPREHENSIVE, false);
            assertThat(route).isEqualTo("standard");
        }

        @Test
        @DisplayName("Should route to fast-track for fleet comprehensive drivable")
        void shouldRouteToFastTrackForFleetComprehensive() {
            String route = calculator.determineRoute(false, true, CoverageType.COMPREHENSIVE, true);
            assertThat(route).isEqualTo("fast-track");
        }

        @Test
        @DisplayName("Should route to fast-track for TPL drivable")
        void shouldRouteToFastTrackForTPL() {
            String route = calculator.determineRoute(false, true, CoverageType.TPL, false);
            assertThat(route).isEqualTo("fast-track");
        }

        @Test
        @DisplayName("Should route to standard for non-fleet comprehensive drivable")
        void shouldRouteToStandardForNonFleetComprehensive() {
            String route = calculator.determineRoute(false, true, CoverageType.COMPREHENSIVE, false);
            assertThat(route).isEqualTo("standard");
        }
    }

    @Nested
    @DisplayName("Severity Description Tests")
    class SeverityDescriptionTests {

        @Test
        @DisplayName("Should return high severity description")
        void shouldReturnHighSeverityDescription() {
            String description = calculator.getSeverityDescription("HIGH");
            assertThat(description).contains("High priority");
            assertThat(description).contains("injury");
        }

        @Test
        @DisplayName("Should return medium severity description")
        void shouldReturnMediumSeverityDescription() {
            String description = calculator.getSeverityDescription("MEDIUM");
            assertThat(description).contains("Medium priority");
        }

        @Test
        @DisplayName("Should return low severity description")
        void shouldReturnLowSeverityDescription() {
            String description = calculator.getSeverityDescription("LOW");
            assertThat(description).contains("Low priority");
            assertThat(description).contains("fast-track");
        }
    }

    @Nested
    @DisplayName("Route Description Tests")
    class RouteDescriptionTests {

        @Test
        @DisplayName("Should return complex route description")
        void shouldReturnComplexRouteDescription() {
            String description = calculator.getRouteDescription("complex");
            assertThat(description).contains("senior");
        }

        @Test
        @DisplayName("Should return standard route description")
        void shouldReturnStandardRouteDescription() {
            String description = calculator.getRouteDescription("standard");
            assertThat(description).contains("claims team");
        }

        @Test
        @DisplayName("Should return fast-track route description")
        void shouldReturnFastTrackRouteDescription() {
            String description = calculator.getRouteDescription("fast-track");
            assertThat(description).contains("Fast-track");
        }
    }
}
