/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FnolDetailResponse(
        // Identifiers
        String fnolId,
        String processInstanceKey,

        // Country & Contact
        String countryCode,
        String mobileNumber,
        String nationalId,
        String reporterName,
        String reporterEmail,

        // Vehicle Information
        String plateNumber,
        String vehicleType,
        String vehicleMake,
        String vehicleModel,
        Integer vehicleYear,
        String vehicleColor,

        // Policy Information
        String policyNumber,
        String coverageType,
        boolean isFleet,

        // Incident Details
        LocalDate incidentDate,
        LocalTime incidentTime,
        String incidentLocation,
        Double latitude,
        Double longitude,
        String description,

        // Incident Assessment
        boolean isDrivable,
        boolean hasInjuries,
        boolean thirdPartyInvolved,
        String policeReportNumber,

        // Preferences
        String preferredLanguage,

        // Attachments
        List<AttachmentResponse> attachments,

        // Processing Status
        String status,
        String severityLevel,
        String route,

        // Timestamps
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    /**
     * Attachment in the response.
     */
    public record AttachmentResponse(
            String url,
            String type,
            String description
    ) {}
}
