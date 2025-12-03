/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.domain.enums;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;

public enum GccCountry {

    /**
     * United Arab Emirates
     * Currency: UAE Dirham (AED) - Symbol: د.إ
     * Vehicle Registration: Mulkiya (ملكية)
     * National ID: Emirates ID
     */
    AE("United Arab Emirates", "UAE", "+971", "Asia/Dubai",
       "AED", "د.إ", "Emirates ID", "Mulkiya"),

    /**
     * Saudi Arabia
     * Currency: Saudi Riyal (SAR) - Symbol: ر.س
     * Vehicle Registration: Istimara (استمارة)
     * National ID: Iqama (for residents) or National ID (for citizens)
     */
    SA("Saudi Arabia", "KSA", "+966", "Asia/Riyadh",
       "SAR", "ر.س", "Iqama/National ID", "Istimara"),

    /**
     * Qatar
     * Currency: Qatari Riyal (QAR) - Symbol: ر.ق
     * Vehicle Registration: Istemara (استمارة)
     * National ID: QID (Qatar ID)
     */
    QA("Qatar", "QAT", "+974", "Asia/Qatar",
       "QAR", "ر.ق", "QID", "Istemara"),

    /**
     * Bahrain
     * Currency: Bahraini Dinar (BHD) - Symbol: د.ب
     * Vehicle Registration: Vehicle Registration Card
     * National ID: CPR (Central Population Registry)
     */
    BH("Bahrain", "BHR", "+973", "Asia/Bahrain",
       "BHD", "د.ب", "CPR", "Vehicle Registration Card"),

    /**
     * Kuwait
     * Currency: Kuwaiti Dinar (KWD) - Symbol: د.ك
     * Vehicle Registration: Daftar (دفتر)
     * National ID: Civil ID
     */
    KW("Kuwait", "KWT", "+965", "Asia/Kuwait",
       "KWD", "د.ك", "Civil ID", "Daftar"),

    /**
     * Oman
     * Currency: Omani Rial (OMR) - Symbol: ر.ع
     * Vehicle Registration: Mulkiya (ملكية)
     * National ID: National ID / Resident Card
     */
    OM("Oman", "OMN", "+968", "Asia/Muscat",
       "OMR", "ر.ع", "National ID", "Mulkiya");

    private final String fullName;
    private final String shortCode;
    private final String phonePrefix;
    private final String timezone;
    private final String currencyCode;
    private final String currencySymbol;
    private final String nationalIdName;
    private final String vehicleRegistrationName;

    GccCountry(String fullName, String shortCode, String phonePrefix, String timezone,
               String currencyCode, String currencySymbol, String nationalIdName,
               String vehicleRegistrationName) {
        this.fullName = fullName;
        this.shortCode = shortCode;
        this.phonePrefix = phonePrefix;
        this.timezone = timezone;
        this.currencyCode = currencyCode;
        this.currencySymbol = currencySymbol;
        this.nationalIdName = nationalIdName;
        this.vehicleRegistrationName = vehicleRegistrationName;
    }

    /**
     * Get the full name of the country.
     *
     * @return full country name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Get the short code (3-letter) of the country.
     *
     * @return 3-letter country code
     */
    public String getShortCode() {
        return shortCode;
    }

    /**
     * Get the phone prefix for the country.
     *
     * @return phone prefix (e.g., +971)
     */
    public String getPhonePrefix() {
        return phonePrefix;
    }

    /**
     * Get the timezone ID for the country.
     *
     * @return timezone string (e.g., Asia/Dubai)
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * Get the ZoneId for the country's timezone.
     *
     * @return ZoneId instance
     */
    public ZoneId getZoneId() {
        return ZoneId.of(timezone);
    }

    /**
     * Get the ISO 4217 currency code.
     *
     * @return currency code (e.g., AED, SAR, KWD)
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Get the currency symbol in Arabic.
     *
     * @return Arabic currency symbol (e.g., د.إ for AED)
     */
    public String getCurrencySymbol() {
        return currencySymbol;
    }

    /**
     * Get the country-specific name for national ID.
     *
     * @return national ID name (e.g., Emirates ID, Iqama, Civil ID, QID, CPR)
     */
    public String getNationalIdName() {
        return nationalIdName;
    }

    /**
     * Get the country-specific name for vehicle registration document.
     *
     * @return vehicle registration name (e.g., Mulkiya, Istimara, Daftar)
     */
    public String getVehicleRegistrationName() {
        return vehicleRegistrationName;
    }

    /**
     * Format an amount with the country's currency.
     *
     * @param amount the amount to format
     * @return formatted string (e.g., "1,000.00 AED" or "د.إ 1,000.00")
     */
    public String formatCurrency(double amount) {
        return String.format("%,.2f %s", amount, currencyCode);
    }

    /**
     * Format an amount with the country's currency symbol (Arabic).
     *
     * @param amount the amount to format
     * @return formatted string with Arabic symbol (e.g., "د.إ 1,000.00")
     */
    public String formatCurrencyArabic(double amount) {
        return String.format("%s %,.2f", currencySymbol, amount);
    }

    /**
     * Find a country by its ISO 2-letter code.
     *
     * @param code ISO 2-letter country code (case-insensitive)
     * @return Optional containing the country if found
     */
    public static Optional<GccCountry> fromCode(String code) {
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
     * Find a country by its phone prefix.
     *
     * @param prefix phone prefix (e.g., +971)
     * @return Optional containing the country if found
     */
    public static Optional<GccCountry> fromPhonePrefix(String prefix) {
        if (prefix == null || prefix.isBlank()) {
            return Optional.empty();
        }
        String normalizedPrefix = prefix.trim();
        return Arrays.stream(values())
                .filter(c -> c.phonePrefix.equals(normalizedPrefix))
                .findFirst();
    }

    /**
     * Check if a given country code is a valid GCC country.
     *
     * @param code country code to check
     * @return true if valid GCC country
     */
    public static boolean isValidGccCountry(String code) {
        return fromCode(code).isPresent();
    }
}
