/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.application.service;

import com.ibm.icu.text.Normalizer2;
import io.camunda.community.fnol.gcc.motor.domain.enums.LanguageCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Pattern;

@Service
public class LanguageNormalizationService {

    private static final Logger log = LoggerFactory.getLogger(LanguageNormalizationService.class);

    // Arabic diacritics (tashkeel) range: U+064B to U+065F
    private static final Pattern ARABIC_DIACRITICS = Pattern.compile("[\\u064B-\\u065F]");

    // Arabic tatweel (kashida): U+0640
    private static final Pattern ARABIC_TATWEEL = Pattern.compile("\\u0640");

    // Multiple whitespace
    private static final Pattern MULTIPLE_WHITESPACE = Pattern.compile("\\s+");

    // Eastern Arabic to Western Arabic numeral mapping
    private static final Map<Character, Character> EASTERN_TO_WESTERN_NUMERALS = Map.of(
            '٠', '0',
            '١', '1',
            '٢', '2',
            '٣', '3',
            '٤', '4',
            '٥', '5',
            '٦', '6',
            '٧', '7',
            '٨', '8',
            '٩', '9'
    );

    // Hamza normalization mapping
    private static final Map<Character, Character> HAMZA_NORMALIZATION = Map.of(
            'أ', 'ا',  // Alef with Hamza above
            'إ', 'ا',  // Alef with Hamza below
            'آ', 'ا',  // Alef with Madda
            'ٱ', 'ا',  // Alef Wasla
            'ؤ', 'و',  // Waw with Hamza (optional, some prefer to keep)
            'ئ', 'ي'   // Yeh with Hamza (optional, some prefer to keep)
    );

    private final Normalizer2 nfcNormalizer;

    public LanguageNormalizationService() {
        this.nfcNormalizer = Normalizer2.getNFCInstance();
    }

    /**
     * Normalize text with auto-detection of language.
     *
     * @param text the text to normalize
     * @return normalized text
     */
    public String normalize(String text) {
        if (text == null || text.isBlank()) {
            return text;
        }
        // Auto-detect: if contains Arabic, use Arabic normalization
        if (containsArabic(text)) {
            return normalizeArabic(text);
        }
        return normalizeBasic(text);
    }

    /**
     * Normalize text based on the detected or specified language.
     *
     * @param text         the text to normalize
     * @param languageCode the language code
     * @return normalized text
     */
    public String normalize(String text, LanguageCode languageCode) {
        if (text == null || text.isBlank()) {
            return text;
        }

        String normalized = text;

        // Apply language-specific normalization
        if (languageCode == LanguageCode.AR || containsArabic(text)) {
            normalized = normalizeArabic(text);
        } else if (languageCode == LanguageCode.UR) {
            // Urdu uses Arabic script, apply similar normalization
            normalized = normalizeArabic(text);
        } else {
            // Basic normalization for other languages
            normalized = normalizeBasic(text);
        }

        log.debug("Normalized text from '{}' to '{}'", text, normalized);
        return normalized;
    }

    /**
     * Normalize Arabic text.
     *
     * @param text the Arabic text
     * @return normalized text
     */
    public String normalizeArabic(String text) {
        if (text == null || text.isBlank()) {
            return text;
        }

        String result = text;

        // Step 1: Unicode NFC normalization
        result = nfcNormalizer.normalize(result);

        // Step 2: Remove tashkeel (diacritical marks)
        result = removeTashkeel(result);

        // Step 3: Remove tatweel (kashida)
        result = removeTatweel(result);

        // Step 4: Normalize hamza variants
        result = normalizeHamza(result);

        // Step 5: Convert Eastern Arabic numerals to Western
        result = convertEasternNumerals(result);

        // Step 6: Normalize whitespace
        result = normalizeWhitespace(result);

        return result;
    }

    /**
     * Basic normalization for non-Arabic text.
     *
     * @param text the text to normalize
     * @return normalized text
     */
    public String normalizeBasic(String text) {
        if (text == null || text.isBlank()) {
            return text;
        }

        String result = text;

        // Unicode NFC normalization
        result = nfcNormalizer.normalize(result);

        // Convert Eastern Arabic numerals (in case of mixed text)
        result = convertEasternNumerals(result);

        // Normalize whitespace
        result = normalizeWhitespace(result);

        return result;
    }

    /**
     * Remove Arabic tashkeel (diacritical marks).
     *
     * @param text the text
     * @return text without tashkeel
     */
    public String removeTashkeel(String text) {
        if (text == null) {
            return null;
        }
        return ARABIC_DIACRITICS.matcher(text).replaceAll("");
    }

    /**
     * Remove Arabic tatweel (kashida/elongation character).
     *
     * @param text the text
     * @return text without tatweel
     */
    public String removeTatweel(String text) {
        if (text == null) {
            return null;
        }
        return ARABIC_TATWEEL.matcher(text).replaceAll("");
    }

    /**
     * Normalize hamza variants to their base forms.
     *
     * @param text the text
     * @return text with normalized hamza
     */
    public String normalizeHamza(String text) {
        if (text == null) {
            return null;
        }

        StringBuilder result = new StringBuilder(text.length());
        for (char c : text.toCharArray()) {
            result.append(HAMZA_NORMALIZATION.getOrDefault(c, c));
        }
        return result.toString();
    }

    /**
     * Convert Eastern Arabic numerals (٠-٩) to Western Arabic numerals (0-9).
     *
     * @param text the text
     * @return text with Western numerals
     */
    public String convertEasternNumerals(String text) {
        if (text == null) {
            return null;
        }

        StringBuilder result = new StringBuilder(text.length());
        for (char c : text.toCharArray()) {
            result.append(EASTERN_TO_WESTERN_NUMERALS.getOrDefault(c, c));
        }
        return result.toString();
    }

    /**
     * Normalize whitespace (trim and collapse multiple spaces).
     *
     * @param text the text
     * @return text with normalized whitespace
     */
    public String normalizeWhitespace(String text) {
        if (text == null) {
            return null;
        }
        return MULTIPLE_WHITESPACE.matcher(text.trim()).replaceAll(" ");
    }

    /**
     * Check if text contains Arabic characters.
     *
     * @param text the text to check
     * @return true if contains Arabic
     */
    public boolean containsArabic(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        return text.codePoints()
                .anyMatch(cp -> Character.UnicodeBlock.of(cp) == Character.UnicodeBlock.ARABIC);
    }

    /**
     * Check if text is primarily Arabic.
     *
     * @param text the text to check
     * @return true if primarily Arabic
     */
    public boolean isPrimarilyArabic(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }

        long arabicCount = text.codePoints()
                .filter(cp -> Character.UnicodeBlock.of(cp) == Character.UnicodeBlock.ARABIC)
                .count();

        long letterCount = text.codePoints()
                .filter(Character::isLetter)
                .count();

        return letterCount > 0 && (double) arabicCount / letterCount > 0.5;
    }
}
