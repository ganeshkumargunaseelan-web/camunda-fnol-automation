/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record FnolSubmitRequest(
        // Country & Contact
        @NotBlank(message = "Country code is required")
        @Size(min = 2, max = 3, message = "Country code must be 2-3 characters")
        String countryCode,

        @NotBlank(message = "Mobile number is required")
        @Size(max = 20, message = "Mobile number must not exceed 20 characters")
        String mobileNumber,

        @NotBlank(message = "National ID is required")
        @Size(max = 50, message = "National ID must not exceed 50 characters")
        String nationalId,

        @Size(max = 200, message = "Reporter name must not exceed 200 characters")
        String reporterName,

        @Email(message = "Invalid email format")
        @Size(max = 200, message = "Reporter email must not exceed 200 characters")
        String reporterEmail,

        // Vehicle Information
        @NotBlank(message = "Plate number is required")
        @Size(max = 20, message = "Plate number must not exceed 20 characters")
        String plateNumber,

        @Size(min = 2, max = 3, message = "Plate country must be 2-3 characters")
        String plateCountry,

        @Size(max = 20, message = "Vehicle type must not exceed 20 characters")
        String vehicleType,

        @Size(max = 100, message = "Vehicle make must not exceed 100 characters")
        String vehicleMake,

        @Size(max = 100, message = "Vehicle model must not exceed 100 characters")
        String vehicleModel,

        @Min(value = 1900, message = "Vehicle year must be at least 1900")
        @Max(value = 2100, message = "Vehicle year must not exceed 2100")
        Integer vehicleYear,

        @Size(max = 50, message = "Vehicle color must not exceed 50 characters")
        String vehicleColor,

        // Policy Information
        @Size(max = 50, message = "Policy number must not exceed 50 characters")
        String policyNumber,

        @Size(max = 20, message = "Coverage type must not exceed 20 characters")
        String coverageType,

        @JsonProperty("isFleet")
        boolean isFleet,

        // Incident Details
        @NotBlank(message = "Incident date is required")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Incident date must be in YYYY-MM-DD format")
        String incidentDate,

        @Pattern(regexp = "\\d{2}:\\d{2}(:\\d{2})?", message = "Incident time must be in HH:mm or HH:mm:ss format")
        String incidentTime,

        @Size(max = 500, message = "Incident location must not exceed 500 characters")
        String incidentLocation,

        @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
        Double latitude,

        @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
        @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
        Double longitude,

        @Size(max = 5000, message = "Description must not exceed 5000 characters")
        String description,

        // Incident Assessment
        @JsonProperty("isDrivable")
        boolean isDrivable,

        @JsonProperty("hasInjuries")
        boolean hasInjuries,

        boolean thirdPartyInvolved,

        @Size(max = 100, message = "Police report number must not exceed 100 characters")
        String policeReportNumber,

        // Preferences
        @Size(min = 2, max = 10, message = "Preferred language must be 2-10 characters")
        String preferredLanguage,

        // Attachments
        @Valid
        @Size(max = 20, message = "Maximum 20 attachments allowed")
        List<AttachmentRequest> attachments
) {
    /**
     * Attachment within the request.
     */
    public record AttachmentRequest(
            @NotBlank(message = "Attachment URL is required")
            @Size(max = 2048, message = "Attachment URL must not exceed 2048 characters")
            String url,

            @Size(max = 20, message = "Attachment type must not exceed 20 characters")
            String type,

            @Size(max = 500, message = "Attachment description must not exceed 500 characters")
            String description
    ) {}
}
