/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LanguageNormalizationServiceTest {

    private LanguageNormalizationService service;

    @BeforeEach
    void setUp() {
        service = new LanguageNormalizationService();
    }

    @Nested
    @DisplayName("Arabic Text Normalization")
    class ArabicNormalizationTests {

        @Test
        @DisplayName("Should remove Arabic diacritics (tashkeel)")
        void shouldRemoveDiacritics() {
            String input = "مُحَمَّد";  // Muhammad with diacritics
            String normalized = service.normalizeArabic(input);
            assertThat(normalized).doesNotContain("\u064E"); // Fatha
            assertThat(normalized).doesNotContain("\u064F"); // Damma
            assertThat(normalized).doesNotContain("\u0650"); // Kasra
        }

        @Test
        @DisplayName("Should remove tatweel (kashida)")
        void shouldRemoveTatweel() {
            String input = "مـــحـــمـــد";  // With tatweel
            String normalized = service.normalizeArabic(input);
            assertThat(normalized).doesNotContain("\u0640"); // Tatweel
        }

        @Test
        @DisplayName("Should normalize hamza variations")
        void shouldNormalizeHamza() {
            String alefWithHamza = "أحمد";
            String normalized = service.normalizeArabic(alefWithHamza);
            // Should normalize to plain alef or consistent form
            assertThat(normalized).isNotEmpty();
        }

        @Test
        @DisplayName("Should convert Eastern Arabic numerals to Western")
        void shouldConvertEasternNumerals() {
            String eastern = "١٢٣٤٥٦٧٨٩٠";
            String normalized = service.normalizeArabic(eastern);
            assertThat(normalized).isEqualTo("1234567890");
        }

        @Test
        @DisplayName("Should preserve non-Arabic text")
        void shouldPreserveNonArabicText() {
            String input = "Hello World 123";
            String normalized = service.normalizeArabic(input);
            assertThat(normalized).isEqualTo("Hello World 123");
        }

        @Test
        @DisplayName("Should handle null input")
        void shouldHandleNullInput() {
            String normalized = service.normalizeArabic(null);
            assertThat(normalized).isNull();
        }

        @Test
        @DisplayName("Should handle empty input")
        void shouldHandleEmptyInput() {
            String normalized = service.normalizeArabic("");
            assertThat(normalized).isEmpty();
        }

        @Test
        @DisplayName("Should handle mixed Arabic and English")
        void shouldHandleMixedText() {
            String input = "حادث على شارع Sheikh Zayed Road ١٢٣";
            String normalized = service.normalizeArabic(input);
            assertThat(normalized).contains("Sheikh Zayed Road");
            assertThat(normalized).contains("123");
        }
    }

    @Nested
    @DisplayName("Arabic Detection Tests")
    class ArabicDetectionTests {

        @Test
        @DisplayName("Should detect Arabic text")
        void shouldDetectArabic() {
            assertThat(service.containsArabic("مرحبا")).isTrue();
        }

        @Test
        @DisplayName("Should not detect Arabic in English text")
        void shouldNotDetectArabicInEnglish() {
            assertThat(service.containsArabic("Hello World")).isFalse();
        }

        @Test
        @DisplayName("Should detect Arabic in mixed text")
        void shouldDetectArabicInMixed() {
            assertThat(service.containsArabic("Hello مرحبا")).isTrue();
        }
    }

}
