/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fnol_cases")
public class FnolCaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fnol_id", nullable = false, unique = true, length = 50)
    private String fnolId;

    // Country & Contact
    @Column(name = "country", nullable = false, length = 10)
    private String country;

    @Column(name = "mobile_number", nullable = false, length = 20)
    private String mobileNumber;

    @Column(name = "national_id", nullable = false, length = 50)
    private String nationalId;

    @Column(name = "reporter_name", length = 200)
    private String reporterName;

    @Column(name = "reporter_email", length = 200)
    private String reporterEmail;

    // Vehicle Information
    @Column(name = "plate_number", nullable = false, length = 20)
    private String plateNumber;

    @Column(name = "plate_country", nullable = false, length = 10)
    private String plateCountry;

    @Column(name = "vehicle_type", nullable = false, length = 20)
    private String vehicleType;

    @Column(name = "vehicle_make", length = 100)
    private String vehicleMake;

    @Column(name = "vehicle_model", length = 100)
    private String vehicleModel;

    @Column(name = "vehicle_year")
    private Integer vehicleYear;

    @Column(name = "vehicle_color", length = 50)
    private String vehicleColor;

    // Policy Information
    @Column(name = "policy_number", length = 50)
    private String policyNumber;

    @Column(name = "coverage_type", nullable = false, length = 20)
    private String coverageType;

    @Column(name = "fleet_flag", nullable = false)
    private boolean fleetFlag;

    // Incident Details
    @Column(name = "incident_date", nullable = false)
    private LocalDate incidentDate;

    @Column(name = "incident_time")
    private LocalTime incidentTime;

    @Column(name = "incident_location", length = 500)
    private String incidentLocation;

    @Column(name = "incident_latitude", precision = 10)
    private Double incidentLatitude;

    @Column(name = "incident_longitude", precision = 11)
    private Double incidentLongitude;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Incident Assessment
    @Column(name = "drivable", nullable = false)
    private boolean drivable = true;

    @Column(name = "injuries", nullable = false)
    private boolean injuries = false;

    @Column(name = "third_party_involved", nullable = false)
    private boolean thirdPartyInvolved = false;

    @Column(name = "police_report_number", length = 100)
    private String policeReportNumber;

    // Preferences
    @Column(name = "preferred_language", nullable = false, length = 10)
    private String preferredLanguage = "EN";

    // Processing
    @Column(name = "status", nullable = false, length = 50)
    private String status = "SUBMITTED";

    @Column(name = "severity_level", length = 20)
    private String severityLevel;

    @Column(name = "route", length = 50)
    private String route;

    @Column(name = "process_instance_key", length = 100)
    private String processInstanceKey;

    // Audit
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "fnolCase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AttachmentEntity> attachments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFnolId() {
        return fnolId;
    }

    public void setFnolId(String fnolId) {
        this.fnolId = fnolId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public void setReporterEmail(String reporterEmail) {
        this.reporterEmail = reporterEmail;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getPlateCountry() {
        return plateCountry;
    }

    public void setPlateCountry(String plateCountry) {
        this.plateCountry = plateCountry;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public Integer getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(Integer vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getCoverageType() {
        return coverageType;
    }

    public void setCoverageType(String coverageType) {
        this.coverageType = coverageType;
    }

    public boolean isFleetFlag() {
        return fleetFlag;
    }

    public void setFleetFlag(boolean fleetFlag) {
        this.fleetFlag = fleetFlag;
    }

    public LocalDate getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(LocalDate incidentDate) {
        this.incidentDate = incidentDate;
    }

    public LocalTime getIncidentTime() {
        return incidentTime;
    }

    public void setIncidentTime(LocalTime incidentTime) {
        this.incidentTime = incidentTime;
    }

    public String getIncidentLocation() {
        return incidentLocation;
    }

    public void setIncidentLocation(String incidentLocation) {
        this.incidentLocation = incidentLocation;
    }

    public Double getIncidentLatitude() {
        return incidentLatitude;
    }

    public void setIncidentLatitude(Double incidentLatitude) {
        this.incidentLatitude = incidentLatitude;
    }

    public Double getIncidentLongitude() {
        return incidentLongitude;
    }

    public void setIncidentLongitude(Double incidentLongitude) {
        this.incidentLongitude = incidentLongitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDrivable() {
        return drivable;
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

    public boolean isThirdPartyInvolved() {
        return thirdPartyInvolved;
    }

    public void setThirdPartyInvolved(boolean thirdPartyInvolved) {
        this.thirdPartyInvolved = thirdPartyInvolved;
    }

    public String getPoliceReportNumber() {
        return policeReportNumber;
    }

    public void setPoliceReportNumber(String policeReportNumber) {
        this.policeReportNumber = policeReportNumber;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(String severityLevel) {
        this.severityLevel = severityLevel;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getProcessInstanceKey() {
        return processInstanceKey;
    }

    public void setProcessInstanceKey(String processInstanceKey) {
        this.processInstanceKey = processInstanceKey;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<AttachmentEntity> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentEntity> attachments) {
        this.attachments = attachments;
    }

    public void addAttachment(AttachmentEntity attachment) {
        attachments.add(attachment);
        attachment.setFnolCase(this);
    }

    public void removeAttachment(AttachmentEntity attachment) {
        attachments.remove(attachment);
        attachment.setFnolCase(null);
    }
}
