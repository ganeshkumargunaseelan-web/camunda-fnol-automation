/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.domain.model;

import io.camunda.community.fnol.gcc.motor.domain.enums.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MotorFnolCase {

    // ═══════════════════════════════════════════════════════════════════════════════
    // IDENTIFICATION
    // ═══════════════════════════════════════════════════════════════════════════════
    private String fnolId;
    private String correlationId;

    // ═══════════════════════════════════════════════════════════════════════════════
    // POLICY & INSURED
    // ═══════════════════════════════════════════════════════════════════════════════
    private String policyNumber;
    private String insuredName;
    private String nationalId;
    private String mobileNumber;
    private GccCountry country;
    private LanguageCode languageCode;

    // ═══════════════════════════════════════════════════════════════════════════════
    // VEHICLE
    // ═══════════════════════════════════════════════════════════════════════════════
    private String plateNumber;
    private GccCountry plateCountry;
    private CoverageType coverageType;
    private boolean fleetFlag;
    private VehicleType vehicleType;

    // ═══════════════════════════════════════════════════════════════════════════════
    // ACCIDENT
    // ═══════════════════════════════════════════════════════════════════════════════
    private OffsetDateTime lossDateTime;
    private String lossLocationTextOriginal;
    private String lossLocationTextNormalized;
    private String accidentDescriptionOriginal;
    private String accidentDescriptionNormalized;
    private String policeReportNumber;
    private boolean drivable;
    private boolean injuries;

    // ═══════════════════════════════════════════════════════════════════════════════
    // SEVERITY & ROUTING
    // ═══════════════════════════════════════════════════════════════════════════════
    private SeverityFlags severityFlags;

    // ═══════════════════════════════════════════════════════════════════════════════
    // ATTACHMENTS
    // ═══════════════════════════════════════════════════════════════════════════════
    private List<Attachment> attachments = new ArrayList<>();

    // ═══════════════════════════════════════════════════════════════════════════════
    // PROCESS STATE
    // ═══════════════════════════════════════════════════════════════════════════════
    private String processInstanceKey;
    private String processStatus;
    private OffsetDateTime submittedAt;

    /**
     * Default constructor for serialization.
     */
    public MotorFnolCase() {
    }

    /**
     * Private constructor for builder.
     */
    private MotorFnolCase(Builder builder) {
        this.fnolId = builder.fnolId;
        this.correlationId = builder.correlationId;
        this.policyNumber = builder.policyNumber;
        this.insuredName = builder.insuredName;
        this.nationalId = builder.nationalId;
        this.mobileNumber = builder.mobileNumber;
        this.country = builder.country;
        this.languageCode = builder.languageCode;
        this.plateNumber = builder.plateNumber;
        this.plateCountry = builder.plateCountry;
        this.coverageType = builder.coverageType;
        this.fleetFlag = builder.fleetFlag;
        this.vehicleType = builder.vehicleType;
        this.lossDateTime = builder.lossDateTime;
        this.lossLocationTextOriginal = builder.lossLocationTextOriginal;
        this.lossLocationTextNormalized = builder.lossLocationTextNormalized;
        this.accidentDescriptionOriginal = builder.accidentDescriptionOriginal;
        this.accidentDescriptionNormalized = builder.accidentDescriptionNormalized;
        this.policeReportNumber = builder.policeReportNumber;
        this.drivable = builder.drivable;
        this.injuries = builder.injuries;
        this.severityFlags = builder.severityFlags;
        this.attachments = builder.attachments != null ? new ArrayList<>(builder.attachments) : new ArrayList<>();
        this.processInstanceKey = builder.processInstanceKey;
        this.processStatus = builder.processStatus;
        this.submittedAt = builder.submittedAt;
    }

    /**
     * Create a new builder instance.
     *
     * @return new Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // BUSINESS METHODS
    // ═══════════════════════════════════════════════════════════════════════════════

    /**
     * Calculate and set severity flags based on case data.
     */
    public void calculateSeverity() {
        this.severityFlags = SeverityFlags.from(drivable, injuries);
    }

    /**
     * Get the severity level for this case.
     *
     * @return severity level (HIGH, MEDIUM, LOW)
     */
    public String getSeverityLevel() {
        return severityFlags != null ? severityFlags.getSeverityLevel() : "LOW";
    }

    /**
     * Get the routing destination for this case.
     *
     * @return route (complex, standard, fast-track)
     */
    public String getRoute() {
        return severityFlags != null ? severityFlags.getRoute() : "fast-track";
    }

    /**
     * Check if this case has injuries reported.
     *
     * @return true if injuries
     */
    public boolean hasInjuries() {
        return injuries;
    }

    /**
     * Check if the vehicle is drivable.
     *
     * @return true if drivable
     */
    public boolean isDrivable() {
        return drivable;
    }

    /**
     * Check if this is a fleet vehicle.
     *
     * @return true if fleet
     */
    public boolean isFleetVehicle() {
        return fleetFlag;
    }

    /**
     * Check if this case has comprehensive coverage.
     *
     * @return true if comprehensive
     */
    public boolean hasComprehensiveCoverage() {
        return coverageType == CoverageType.COMPREHENSIVE;
    }

    /**
     * Add an attachment to this case.
     *
     * @param attachment the attachment to add
     */
    public void addAttachment(Attachment attachment) {
        Objects.requireNonNull(attachment, "Attachment cannot be null");
        this.attachments.add(attachment);
    }

    /**
     * Get an unmodifiable list of attachments.
     *
     * @return attachments list
     */
    public List<Attachment> getAttachments() {
        return Collections.unmodifiableList(attachments);
    }

    /**
     * Get attachment count.
     *
     * @return number of attachments
     */
    public int getAttachmentCount() {
        return attachments.size();
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // GETTERS AND SETTERS
    // ═══════════════════════════════════════════════════════════════════════════════

    public String getFnolId() {
        return fnolId;
    }

    public void setFnolId(String fnolId) {
        this.fnolId = fnolId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getInsuredName() {
        return insuredName;
    }

    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public GccCountry getCountry() {
        return country;
    }

    public void setCountry(GccCountry country) {
        this.country = country;
    }

    public LanguageCode getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(LanguageCode languageCode) {
        this.languageCode = languageCode;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public GccCountry getPlateCountry() {
        return plateCountry;
    }

    public void setPlateCountry(GccCountry plateCountry) {
        this.plateCountry = plateCountry;
    }

    public CoverageType getCoverageType() {
        return coverageType;
    }

    public void setCoverageType(CoverageType coverageType) {
        this.coverageType = coverageType;
    }

    public boolean isFleetFlag() {
        return fleetFlag;
    }

    public void setFleetFlag(boolean fleetFlag) {
        this.fleetFlag = fleetFlag;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public OffsetDateTime getLossDateTime() {
        return lossDateTime;
    }

    public void setLossDateTime(OffsetDateTime lossDateTime) {
        this.lossDateTime = lossDateTime;
    }

    public String getLossLocationTextOriginal() {
        return lossLocationTextOriginal;
    }

    public void setLossLocationTextOriginal(String lossLocationTextOriginal) {
        this.lossLocationTextOriginal = lossLocationTextOriginal;
    }

    public String getLossLocationTextNormalized() {
        return lossLocationTextNormalized;
    }

    public void setLossLocationTextNormalized(String lossLocationTextNormalized) {
        this.lossLocationTextNormalized = lossLocationTextNormalized;
    }

    public String getAccidentDescriptionOriginal() {
        return accidentDescriptionOriginal;
    }

    public void setAccidentDescriptionOriginal(String accidentDescriptionOriginal) {
        this.accidentDescriptionOriginal = accidentDescriptionOriginal;
    }

    public String getAccidentDescriptionNormalized() {
        return accidentDescriptionNormalized;
    }

    public void setAccidentDescriptionNormalized(String accidentDescriptionNormalized) {
        this.accidentDescriptionNormalized = accidentDescriptionNormalized;
    }

    public String getPoliceReportNumber() {
        return policeReportNumber;
    }

    public void setPoliceReportNumber(String policeReportNumber) {
        this.policeReportNumber = policeReportNumber;
    }

    public void setDrivable(boolean drivable) {
        this.drivable = drivable;
    }

    public boolean isInjuries() {
        return injuries;
    }

    public void setInjuries(boolean injuries) {
        this.injuries = injuries;
    }

    public SeverityFlags getSeverityFlags() {
        return severityFlags;
    }

    public void setSeverityFlags(SeverityFlags severityFlags) {
        this.severityFlags = severityFlags;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments != null ? new ArrayList<>(attachments) : new ArrayList<>();
    }

    public String getProcessInstanceKey() {
        return processInstanceKey;
    }

    public void setProcessInstanceKey(String processInstanceKey) {
        this.processInstanceKey = processInstanceKey;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public OffsetDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(OffsetDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // BUILDER
    // ═══════════════════════════════════════════════════════════════════════════════

    /**
     * Builder for MotorFnolCase.
     */
    public static class Builder {
        private String fnolId;
        private String correlationId;
        private String policyNumber;
        private String insuredName;
        private String nationalId;
        private String mobileNumber;
        private GccCountry country;
        private LanguageCode languageCode;
        private String plateNumber;
        private GccCountry plateCountry;
        private CoverageType coverageType;
        private boolean fleetFlag;
        private VehicleType vehicleType;
        private OffsetDateTime lossDateTime;
        private String lossLocationTextOriginal;
        private String lossLocationTextNormalized;
        private String accidentDescriptionOriginal;
        private String accidentDescriptionNormalized;
        private String policeReportNumber;
        private boolean drivable = true;
        private boolean injuries = false;
        private SeverityFlags severityFlags;
        private List<Attachment> attachments = new ArrayList<>();
        private String processInstanceKey;
        private String processStatus;
        private OffsetDateTime submittedAt;

        public Builder fnolId(String fnolId) {
            this.fnolId = fnolId;
            return this;
        }

        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Builder policyNumber(String policyNumber) {
            this.policyNumber = policyNumber;
            return this;
        }

        public Builder insuredName(String insuredName) {
            this.insuredName = insuredName;
            return this;
        }

        public Builder nationalId(String nationalId) {
            this.nationalId = nationalId;
            return this;
        }

        public Builder mobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
            return this;
        }

        public Builder country(GccCountry country) {
            this.country = country;
            return this;
        }

        public Builder languageCode(LanguageCode languageCode) {
            this.languageCode = languageCode;
            return this;
        }

        public Builder plateNumber(String plateNumber) {
            this.plateNumber = plateNumber;
            return this;
        }

        public Builder plateCountry(GccCountry plateCountry) {
            this.plateCountry = plateCountry;
            return this;
        }

        public Builder coverageType(CoverageType coverageType) {
            this.coverageType = coverageType;
            return this;
        }

        public Builder fleetFlag(boolean fleetFlag) {
            this.fleetFlag = fleetFlag;
            return this;
        }

        public Builder vehicleType(VehicleType vehicleType) {
            this.vehicleType = vehicleType;
            return this;
        }

        public Builder lossDateTime(OffsetDateTime lossDateTime) {
            this.lossDateTime = lossDateTime;
            return this;
        }

        public Builder lossLocationTextOriginal(String lossLocationTextOriginal) {
            this.lossLocationTextOriginal = lossLocationTextOriginal;
            return this;
        }

        public Builder lossLocationTextNormalized(String lossLocationTextNormalized) {
            this.lossLocationTextNormalized = lossLocationTextNormalized;
            return this;
        }

        public Builder accidentDescriptionOriginal(String accidentDescriptionOriginal) {
            this.accidentDescriptionOriginal = accidentDescriptionOriginal;
            return this;
        }

        public Builder accidentDescriptionNormalized(String accidentDescriptionNormalized) {
            this.accidentDescriptionNormalized = accidentDescriptionNormalized;
            return this;
        }

        public Builder policeReportNumber(String policeReportNumber) {
            this.policeReportNumber = policeReportNumber;
            return this;
        }

        public Builder drivable(boolean drivable) {
            this.drivable = drivable;
            return this;
        }

        public Builder injuries(boolean injuries) {
            this.injuries = injuries;
            return this;
        }

        public Builder severityFlags(SeverityFlags severityFlags) {
            this.severityFlags = severityFlags;
            return this;
        }

        public Builder attachments(List<Attachment> attachments) {
            this.attachments = attachments;
            return this;
        }

        public Builder addAttachment(Attachment attachment) {
            if (this.attachments == null) {
                this.attachments = new ArrayList<>();
            }
            this.attachments.add(attachment);
            return this;
        }

        public Builder processInstanceKey(String processInstanceKey) {
            this.processInstanceKey = processInstanceKey;
            return this;
        }

        public Builder processStatus(String processStatus) {
            this.processStatus = processStatus;
            return this;
        }

        public Builder submittedAt(OffsetDateTime submittedAt) {
            this.submittedAt = submittedAt;
            return this;
        }

        public MotorFnolCase build() {
            MotorFnolCase fnolCase = new MotorFnolCase(this);
            // Auto-calculate severity if not set
            if (fnolCase.severityFlags == null) {
                fnolCase.calculateSeverity();
            }
            return fnolCase;
        }
    }
}
