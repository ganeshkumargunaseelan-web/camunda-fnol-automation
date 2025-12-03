/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.domain.valueobject;

import io.camunda.community.fnol.gcc.motor.domain.enums.GccCountry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GccMobileNumberTest {

    @Nested
    @DisplayName("UAE Mobile Number Tests")
    class UaeMobileNumberTests {

        @Test
        @DisplayName("Should accept valid UAE mobile with international format")
        void shouldAcceptValidUaeInternational() {
            GccMobileNumber number = new GccMobileNumber("+971501234567", GccCountry.AE);
            assertThat(number.value()).isEqualTo("+971501234567");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "+971 50 123 4567",
                "+971-50-123-4567",
                "00971501234567"
        })
        @DisplayName("Should normalize various UAE number formats")
        void shouldNormalizeUaeFormats(String input) {
            GccMobileNumber number = new GccMobileNumber(input, GccCountry.AE);
            assertThat(number.value()).matches("\\+971\\d{9}");
        }

        @Test
        @DisplayName("Should format UAE number correctly for display")
        void shouldFormatUaeNumber() {
            GccMobileNumber number = new GccMobileNumber("+971501234567", GccCountry.AE);
            assertThat(number.getDisplayFormat()).isEqualTo("+971 50 123 4567");
        }
    }

    @Nested
    @DisplayName("Saudi Arabia Mobile Number Tests")
    class SaudiMobileNumberTests {

        @Test
        @DisplayName("Should accept valid Saudi mobile")
        void shouldAcceptValidSaudiNumber() {
            GccMobileNumber number = new GccMobileNumber("+966501234567", GccCountry.SA);
            assertThat(number.value()).isEqualTo("+966501234567");
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should throw exception for null number")
        void shouldThrowForNullNumber() {
            assertThatThrownBy(() -> new GccMobileNumber(null, GccCountry.AE))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("null");
        }

        @Test
        @DisplayName("Should throw exception for blank number")
        void shouldThrowForBlankNumber() {
            assertThatThrownBy(() -> new GccMobileNumber("   ", GccCountry.AE))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal for same normalized number")
        void shouldBeEqualForSameNumber() {
            GccMobileNumber num1 = new GccMobileNumber("+971501234567", GccCountry.AE);
            GccMobileNumber num2 = new GccMobileNumber("+971 50 123 4567", GccCountry.AE);
            assertThat(num1).isEqualTo(num2);
        }

        @Test
        @DisplayName("Should have same hash code for equal numbers")
        void shouldHaveSameHashCode() {
            GccMobileNumber num1 = new GccMobileNumber("+971501234567", GccCountry.AE);
            GccMobileNumber num2 = new GccMobileNumber("00971501234567", GccCountry.AE);
            assertThat(num1.hashCode()).isEqualTo(num2.hashCode());
        }
    }
}
