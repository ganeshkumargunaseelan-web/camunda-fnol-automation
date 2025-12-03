/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.application.service;

import io.camunda.community.fnol.gcc.motor.application.exception.FnolValidationException;
import io.camunda.community.fnol.gcc.motor.application.exception.FnolValidationException.ValidationError;
import io.camunda.community.fnol.gcc.motor.domain.enums.GccCountry;
import io.camunda.community.fnol.gcc.motor.infrastructure.config.GccCountryProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GccValidationService {

    private static final Logger log = LoggerFactory.getLogger(GccValidationService.class);

    private final GccCountryProperties countryProperties;

    public GccValidationService(GccCountryProperties countryProperties) {
        this.countryProperties = countryProperties;
    }

    /**
     * Validate all GCC-specific fields for an FNOL submission.
     *
     * @param countryCode   the country code
     * @param mobileNumber  the mobile number
     * @param nationalId    the national ID
     * @param plateNumber   the plate number
     * @param plateCountry  the plate country
     * @throws FnolValidationException if validation fails
     */
    public void validateAll(String countryCode, String mobileNumber, String nationalId,
                            String plateNumber, String plateCountry) {
        FnolValidationException.Builder errorBuilder = FnolValidationException.builder();

        // Validate country
        Optional<GccCountry> countryOpt = GccCountry.fromCode(countryCode);
        if (countryOpt.isEmpty()) {
            errorBuilder.addError("country", "INVALID_COUNTRY",
                    "Invalid or unsupported country code: " + countryCode);
        }

        // Validate mobile number
        ValidationResult msisdnResult = validateMobileNumber(mobileNumber, countryCode);
        if (!msisdnResult.isValid()) {
            errorBuilder.addError("mobileNumber", msisdnResult.errorCode(), msisdnResult.message());
        }

        // Validate national ID
        ValidationResult nationalIdResult = validateNationalId(nationalId, countryCode);
        if (!nationalIdResult.isValid()) {
            errorBuilder.addError("nationalId", nationalIdResult.errorCode(), nationalIdResult.message());
        }

        // Validate plate number
        ValidationResult plateResult = validatePlateNumber(plateNumber, plateCountry);
        if (!plateResult.isValid()) {
            errorBuilder.addError("plateNumber", plateResult.errorCode(), plateResult.message());
        }

        errorBuilder.throwIfErrors();
    }

    /**
     * Validate a GCC mobile number (MSISDN).
     *
     * @param mobileNumber the mobile number
     * @param countryCode  the expected country code
     * @return validation result
     */
    public ValidationResult validateMobileNumber(String mobileNumber, String countryCode) {
        if (mobileNumber == null || mobileNumber.isBlank()) {
            return ValidationResult.invalid("REQUIRED", "Mobile number is required");
        }

        // Normalize the number
        String normalized = mobileNumber.replaceAll("[\\s\\-().]+", "");
        if (!normalized.startsWith("+") && normalized.startsWith("00")) {
            normalized = "+" + normalized.substring(2);
        }

        // Get country config
        Optional<GccCountryProperties.CountryConfig> configOpt = countryProperties.getCountry(countryCode);
        if (configOpt.isEmpty()) {
            // Fall back to generic validation
            if (!normalized.matches("^\\+[0-9]{10,15}$")) {
                return ValidationResult.invalid("INVALID_FORMAT",
                        "Invalid mobile number format. Expected international format: +XXXXXXXXXXXX");
            }
            return ValidationResult.validResult();
        }

        GccCountryProperties.CountryConfig config = configOpt.get();

        // Check prefix
        if (!normalized.startsWith(config.msisdnPrefix())) {
            return ValidationResult.invalid("INVALID_PREFIX",
                    "Mobile number must start with " + config.msisdnPrefix() + " for " + config.name());
        }

        // Check length
        if (normalized.length() != config.msisdnLength()) {
            return ValidationResult.invalid("INVALID_LENGTH",
                    "Mobile number must be " + config.msisdnLength() + " digits for " + config.name());
        }

        log.debug("Mobile number '{}' validated successfully for {}", mobileNumber, countryCode);
        return ValidationResult.validResult();
    }

    /**
     * Validate a GCC national ID.
     *
     * @param nationalId  the national ID
     * @param countryCode the country code
     * @return validation result
     */
    public ValidationResult validateNationalId(String nationalId, String countryCode) {
        if (nationalId == null || nationalId.isBlank()) {
            return ValidationResult.invalid("REQUIRED", "National ID is required");
        }

        Optional<GccCountryProperties.CountryConfig> configOpt = countryProperties.getCountry(countryCode);
        if (configOpt.isEmpty()) {
            // No specific validation available
            return ValidationResult.validResult();
        }

        GccCountryProperties.CountryConfig config = configOpt.get();

        if (!config.isValidNationalId(nationalId)) {
            return ValidationResult.invalid("INVALID_FORMAT",
                    "Invalid " + config.nationalIdName() + " format for " + config.name());
        }

        log.debug("National ID validated successfully for {}", countryCode);
        return ValidationResult.validResult();
    }

    /**
     * Validate a plate number.
     *
     * @param plateNumber  the plate number
     * @param countryCode  the country code
     * @return validation result
     */
    public ValidationResult validatePlateNumber(String plateNumber, String countryCode) {
        if (plateNumber == null || plateNumber.isBlank()) {
            return ValidationResult.invalid("REQUIRED", "Plate number is required");
        }

        Optional<GccCountryProperties.CountryConfig> configOpt = countryProperties.getCountry(countryCode);
        if (configOpt.isEmpty()) {
            // No specific validation available
            return ValidationResult.validResult();
        }

        GccCountryProperties.CountryConfig config = configOpt.get();

        if (!config.isValidPlateNumber(plateNumber)) {
            return ValidationResult.invalid("INVALID_FORMAT",
                    "Invalid plate number format for " + config.name());
        }

        log.debug("Plate number validated successfully for {}", countryCode);
        return ValidationResult.validResult();
    }

    /**
     * Get the national ID type name for a country.
     *
     * @param countryCode the country code
     * @return ID type name
     */
    public String getNationalIdTypeName(String countryCode) {
        return countryProperties.getCountry(countryCode)
                .map(GccCountryProperties.CountryConfig::nationalIdName)
                .orElse("National ID");
    }

    /**
     * Check if a country is supported.
     *
     * @param countryCode the country code
     * @return true if supported
     */
    public boolean isCountrySupported(String countryCode) {
        return GccCountry.isValidGccCountry(countryCode);
    }

    /**
     * Get all supported country codes.
     *
     * @return list of country codes
     */
    public List<String> getSupportedCountries() {
        List<String> countries = new ArrayList<>();
        for (GccCountry country : GccCountry.values()) {
            countries.add(country.name());
        }
        return countries;
    }

    /**
     * Result of a validation check.
     */
    public record ValidationResult(
            boolean valid,
            String errorCode,
            String message
    ) {
        public boolean isValid() {
            return valid;
        }

        public static ValidationResult validResult() {
            return new ValidationResult(true, null, null);
        }

        public static ValidationResult invalid(String errorCode, String message) {
            return new ValidationResult(false, errorCode, message);
        }
    }
}
