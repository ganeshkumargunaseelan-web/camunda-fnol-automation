/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.domain.enums;

import java.util.Locale;
import java.util.Optional;

public enum LanguageCode {

    /**
     * Arabic - official language of all GCC countries.
     */
    AR("Arabic", "العربية", true, new Locale("ar")),

    /**
     * English - widely used business language in GCC.
     */
    EN("English", "English", false, Locale.ENGLISH),

    /**
     * Hindi - spoken by large expatriate community.
     */
    HI("Hindi", "हिन्दी", false, new Locale("hi")),

    /**
     * Urdu - spoken by Pakistani/Indian expatriate community.
     */
    UR("Urdu", "اردو", true, new Locale("ur")),

    /**
     * Malayalam - spoken by Kerala (India) expatriate community.
     */
    ML("Malayalam", "മലയാളം", false, new Locale("ml")),

    /**
     * Tagalog/Filipino - spoken by Filipino expatriate community.
     */
    TL("Tagalog", "Tagalog", false, new Locale("tl"));

    private final String englishName;
    private final String nativeName;
    private final boolean rtl;
    private final Locale locale;

    LanguageCode(String englishName, String nativeName, boolean rtl, Locale locale) {
        this.englishName = englishName;
        this.nativeName = nativeName;
        this.rtl = rtl;
        this.locale = locale;
    }

    /**
     * Get the language name in English.
     *
     * @return English language name
     */
    public String getEnglishName() {
        return englishName;
    }

    /**
     * Get the language name in its native script.
     *
     * @return native language name
     */
    public String getNativeName() {
        return nativeName;
    }

    /**
     * Check if this language uses right-to-left text direction.
     *
     * @return true if RTL language
     */
    public boolean isRtl() {
        return rtl;
    }

    /**
     * Get the Java Locale for this language.
     *
     * @return Locale instance
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Get the ISO language code (lowercase).
     *
     * @return lowercase language code
     */
    public String getCode() {
        return name().toLowerCase();
    }

    /**
     * Find language code from string value.
     *
     * @param code the code to parse (case-insensitive)
     * @return Optional containing language code if found
     */
    public static Optional<LanguageCode> fromCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(valueOf(code.toUpperCase().trim()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * Get the default language code.
     *
     * @return English as default
     */
    public static LanguageCode getDefault() {
        return EN;
    }

    /**
     * Check if a given code is a valid supported language.
     *
     * @param code language code to check
     * @return true if valid
     */
    public static boolean isSupported(String code) {
        return fromCode(code).isPresent();
    }
}
