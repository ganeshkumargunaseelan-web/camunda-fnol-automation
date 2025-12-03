/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.application.port.in;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface SubmitFnolUseCase {

    /**
     * Submit a new FNOL case.
     *
     * @param command the submit command containing all FNOL data
     * @return result containing FNOL ID and process status
     */
    FnolSubmissionResult submit(FnolSubmissionCommand command);

    /**
     * Get the status of an existing FNOL case.
     *
     * @param fnolId the FNOL ID
     * @return status result if found
     */
    Optional<FnolStatusResult> getStatus(String fnolId);

    /**
     * Get the full details of an existing FNOL case.
     *
     * @param fnolId the FNOL ID
     * @return detail result if found
     */
    Optional<FnolDetailResult> getDetail(String fnolId);

    /**
     * Command object for submitting an FNOL.
     */
    record FnolSubmissionCommand(
            // Idempotency & Context
            String idempotencyKey,
            String correlationId,

            // Country & Contact
            String countryCode,
            String mobileNumber,
            String nationalId,
            String reporterName,
            String reporterEmail,

            // Vehicle Information
            String plateNumber,
            String plateCountry,
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
            List<AttachmentData> attachments
    ) {
        /**
         * Create a builder for the command.
         */
        public static Builder builder() {
            return new Builder();
        }

        /**
         * Builder for FnolSubmissionCommand.
         */
        public static class Builder {
            private String idempotencyKey;
            private String correlationId;
            private String countryCode;
            private String mobileNumber;
            private String nationalId;
            private String reporterName;
            private String reporterEmail;
            private String plateNumber;
            private String plateCountry;
            private String vehicleType;
            private String vehicleMake;
            private String vehicleModel;
            private Integer vehicleYear;
            private String vehicleColor;
            private String policyNumber;
            private String coverageType;
            private boolean isFleet;
            private LocalDate incidentDate;
            private LocalTime incidentTime;
            private String incidentLocation;
            private Double latitude;
            private Double longitude;
            private String description;
            private boolean isDrivable = true;
            private boolean hasInjuries = false;
            private boolean thirdPartyInvolved = false;
            private String policeReportNumber;
            private String preferredLanguage = "EN";
            private List<AttachmentData> attachments = List.of();

            public Builder idempotencyKey(String idempotencyKey) {
                this.idempotencyKey = idempotencyKey;
                return this;
            }

            public Builder correlationId(String correlationId) {
                this.correlationId = correlationId;
                return this;
            }

            public Builder countryCode(String countryCode) {
                this.countryCode = countryCode;
                return this;
            }

            public Builder mobileNumber(String mobileNumber) {
                this.mobileNumber = mobileNumber;
                return this;
            }

            public Builder nationalId(String nationalId) {
                this.nationalId = nationalId;
                return this;
            }

            public Builder reporterName(String reporterName) {
                this.reporterName = reporterName;
                return this;
            }

            public Builder reporterEmail(String reporterEmail) {
                this.reporterEmail = reporterEmail;
                return this;
            }

            public Builder plateNumber(String plateNumber) {
                this.plateNumber = plateNumber;
                return this;
            }

            public Builder plateCountry(String plateCountry) {
                this.plateCountry = plateCountry;
                return this;
            }

            public Builder vehicleType(String vehicleType) {
                this.vehicleType = vehicleType;
                return this;
            }

            public Builder vehicleMake(String vehicleMake) {
                this.vehicleMake = vehicleMake;
                return this;
            }

            public Builder vehicleModel(String vehicleModel) {
                this.vehicleModel = vehicleModel;
                return this;
            }

            public Builder vehicleYear(Integer vehicleYear) {
                this.vehicleYear = vehicleYear;
                return this;
            }

            public Builder vehicleColor(String vehicleColor) {
                this.vehicleColor = vehicleColor;
                return this;
            }

            public Builder policyNumber(String policyNumber) {
                this.policyNumber = policyNumber;
                return this;
            }

            public Builder coverageType(String coverageType) {
                this.coverageType = coverageType;
                return this;
            }

            public Builder isFleet(boolean isFleet) {
                this.isFleet = isFleet;
                return this;
            }

            public Builder incidentDate(LocalDate incidentDate) {
                this.incidentDate = incidentDate;
                return this;
            }

            public Builder incidentTime(LocalTime incidentTime) {
                this.incidentTime = incidentTime;
                return this;
            }

            public Builder incidentLocation(String incidentLocation) {
                this.incidentLocation = incidentLocation;
                return this;
            }

            public Builder latitude(Double latitude) {
                this.latitude = latitude;
                return this;
            }

            public Builder longitude(Double longitude) {
                this.longitude = longitude;
                return this;
            }

            public Builder description(String description) {
                this.description = description;
                return this;
            }

            public Builder isDrivable(boolean isDrivable) {
                this.isDrivable = isDrivable;
                return this;
            }

            public Builder hasInjuries(boolean hasInjuries) {
                this.hasInjuries = hasInjuries;
                return this;
            }

            public Builder thirdPartyInvolved(boolean thirdPartyInvolved) {
                this.thirdPartyInvolved = thirdPartyInvolved;
                return this;
            }

            public Builder policeReportNumber(String policeReportNumber) {
                this.policeReportNumber = policeReportNumber;
                return this;
            }

            public Builder preferredLanguage(String preferredLanguage) {
                this.preferredLanguage = preferredLanguage;
                return this;
            }

            public Builder attachments(List<AttachmentData> attachments) {
                this.attachments = attachments;
                return this;
            }

            public FnolSubmissionCommand build() {
                return new FnolSubmissionCommand(
                        idempotencyKey, correlationId, countryCode, mobileNumber, nationalId,
                        reporterName, reporterEmail, plateNumber, plateCountry, vehicleType,
                        vehicleMake, vehicleModel, vehicleYear, vehicleColor, policyNumber,
                        coverageType, isFleet, incidentDate, incidentTime, incidentLocation,
                        latitude, longitude, description, isDrivable, hasInjuries,
                        thirdPartyInvolved, policeReportNumber, preferredLanguage, attachments
                );
            }
        }
    }

    /**
     * Attachment data within the command.
     */
    record AttachmentData(
            String url,
            String type,
            String description
    ) {}

    /**
     * Result of submitting an FNOL.
     */
    record FnolSubmissionResult(
            String fnolId,
            String status,
            String severityLevel,
            String route,
            String processInstanceKey,
            LocalDateTime createdAt,
            boolean isDuplicate
    ) {}

    /**
     * Status result for an existing FNOL.
     */
    record FnolStatusResult(
            String fnolId,
            String status,
            String severityLevel,
            String route,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

    /**
     * Full detail result for an existing FNOL.
     */
    record FnolDetailResult(
            String fnolId,
            String countryCode,
            String mobileNumber,
            String nationalId,
            String reporterName,
            String reporterEmail,
            String plateNumber,
            String vehicleType,
            String vehicleMake,
            String vehicleModel,
            Integer vehicleYear,
            String vehicleColor,
            String policyNumber,
            String coverageType,
            boolean isFleet,
            LocalDate incidentDate,
            LocalTime incidentTime,
            String incidentLocation,
            Double latitude,
            Double longitude,
            String description,
            boolean isDrivable,
            boolean hasInjuries,
            boolean thirdPartyInvolved,
            String policeReportNumber,
            String preferredLanguage,
            List<AttachmentData> attachments,
            String status,
            String severityLevel,
            String route,
            String processInstanceKey,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}
