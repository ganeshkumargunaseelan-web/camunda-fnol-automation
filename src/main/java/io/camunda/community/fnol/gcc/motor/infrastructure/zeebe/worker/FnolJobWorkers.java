/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.zeebe.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.VariablesAsType;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Zeebe Job Workers for GCC Motor FNOL Process.
 *
 * These workers demonstrate Camunda 8 job worker patterns including:
 * - Variable mapping with @Variable annotation
 * - Auto-completion with return values
 * - Error throwing for boundary events
 * - Logging and metrics
 */
@Component
public class FnolJobWorkers {

    private static final Logger log = LoggerFactory.getLogger(FnolJobWorkers.class);
    private final Random random = new Random();

    // ═══════════════════════════════════════════════════════════════════════════
    // VALIDATION WORKERS
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Validates incoming claim data.
     * Throws VALIDATION_ERROR if data is invalid.
     */
    @JobWorker(type = "validate-claim-data", autoComplete = true)
    public Map<String, Object> validateClaimData(
            final ActivatedJob job,
            @Variable String fnolId,
            @Variable String countryCode,
            @Variable String mobileNumber,
            @Variable String nationalId) {

        log.info("Validating claim data for FNOL: {}", fnolId);

        Map<String, Object> result = new HashMap<>();
        result.put("validationTimestamp", LocalDateTime.now().toString());
        result.put("validationStatus", "PASSED");
        result.put("validatedBy", "SYSTEM");

        // Simulate validation logic
        boolean isValid = mobileNumber != null && !mobileNumber.isEmpty()
                && nationalId != null && !nationalId.isEmpty();

        if (!isValid) {
            log.warn("Validation failed for FNOL: {}", fnolId);
            result.put("validationStatus", "FAILED");
            result.put("validationErrors", "Missing required fields");
            // In real implementation, throw error to trigger boundary event
            // throw new ZeebeBpmnError("VALIDATION_ERROR", "Validation failed");
        }

        log.info("Validation completed for FNOL: {} - Status: {}", fnolId, result.get("validationStatus"));
        return result;
    }

    /**
     * AI-powered automatic validation for fast-track claims.
     * Returns confidence score for auto-approval decision.
     */
    @JobWorker(type = "auto-validate-claim", autoComplete = true)
    public Map<String, Object> autoValidateClaim(
            final ActivatedJob job,
            @Variable String fnolId,
            @Variable String description,
            @Variable Boolean drivable,
            @Variable Boolean injuries) {

        log.info("AI validation for FNOL: {}", fnolId);

        Map<String, Object> result = new HashMap<>();

        // Simulate AI validation with confidence score
        double confidenceScore = calculateConfidenceScore(drivable, injuries);

        result.put("validationConfidence", confidenceScore);
        result.put("aiModelVersion", "v2.1");
        result.put("validationDetails", generateValidationDetails(confidenceScore));

        log.info("AI validation completed for FNOL: {} - Confidence: {}", fnolId, confidenceScore);
        return result;
    }

    private double calculateConfidenceScore(Boolean drivable, Boolean injuries) {
        double score = 0.7; // Base score
        if (Boolean.TRUE.equals(drivable)) score += 0.15;
        if (Boolean.FALSE.equals(injuries)) score += 0.15;
        // Add some randomness to simulate real AI behavior
        score += (random.nextDouble() - 0.5) * 0.1;
        return Math.min(1.0, Math.max(0.0, score));
    }

    private String generateValidationDetails(double confidence) {
        if (confidence >= 0.9) return "High confidence - All checks passed";
        if (confidence >= 0.7) return "Good confidence - Minor flags";
        return "Low confidence - Manual review recommended";
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // APPROVAL WORKERS
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Automatic approval for low-risk claims.
     */
    @JobWorker(type = "auto-approve-claim", autoComplete = true)
    public Map<String, Object> autoApproveClaim(
            final ActivatedJob job,
            @Variable String fnolId,
            @Variable Double validationConfidence) {

        log.info("Auto-approving claim for FNOL: {} with confidence: {}", fnolId, validationConfidence);

        Map<String, Object> result = new HashMap<>();
        result.put("approvalType", "AUTOMATIC");
        result.put("approvalTimestamp", LocalDateTime.now().toString());
        result.put("approvedBy", "SYSTEM_AUTO_APPROVAL");
        result.put("approvalReference", "AUTO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        log.info("Claim auto-approved for FNOL: {}", fnolId);
        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // SERVICE INTEGRATION WORKERS
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Arranges towing service for non-drivable vehicles.
     * May throw TOWING_ERROR if service unavailable.
     */
    @JobWorker(type = "arrange-towing", autoComplete = true)
    public Map<String, Object> arrangeTowing(
            final ActivatedJob job,
            @Variable String fnolId,
            @Variable String incidentLocation,
            @Variable Double latitude,
            @Variable Double longitude) {

        log.info("Arranging towing for FNOL: {} at location: {}", fnolId, incidentLocation);

        Map<String, Object> result = new HashMap<>();

        // Simulate towing service integration
        String towingReference = "TOW-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        int estimatedArrival = 30 + random.nextInt(30); // 30-60 minutes

        result.put("towingReference", towingReference);
        result.put("towingProvider", "GCC Roadside Assistance");
        result.put("estimatedArrivalMinutes", estimatedArrival);
        result.put("dispatchedAt", LocalDateTime.now().toString());
        result.put("pickupLocation", incidentLocation);

        log.info("Towing arranged for FNOL: {} - Reference: {}, ETA: {} mins",
                fnolId, towingReference, estimatedArrival);
        return result;
    }

    /**
     * Notifies medical team for claims with injuries.
     * May throw NOTIFICATION_ERROR if notification fails.
     */
    @JobWorker(type = "notify-medical-team", autoComplete = true)
    public Map<String, Object> notifyMedicalTeam(
            final ActivatedJob job,
            @Variable String fnolId,
            @Variable String mobileNumber,
            @Variable String incidentLocation) {

        log.info("Notifying medical team for FNOL: {}", fnolId);

        Map<String, Object> result = new HashMap<>();

        // Simulate medical team notification
        String notificationId = "MED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        result.put("medicalNotificationId", notificationId);
        result.put("notifiedTeam", "Emergency Medical Response");
        result.put("notificationStatus", "SENT");
        result.put("notifiedAt", LocalDateTime.now().toString());
        result.put("responseExpected", "URGENT");

        log.info("Medical team notified for FNOL: {} - Notification ID: {}", fnolId, notificationId);
        return result;
    }

    /**
     * Sends customer notification (SMS/Email/Push).
     */
    @JobWorker(type = "send-customer-notification", autoComplete = true)
    public Map<String, Object> sendCustomerNotification(
            final ActivatedJob job,
            @Variable String fnolId,
            @Variable String mobileNumber,
            @Variable String reporterEmail,
            @Variable String preferredLanguage) {

        log.info("Sending customer notification for FNOL: {}", fnolId);

        Map<String, Object> result = new HashMap<>();

        // Simulate notification sending
        String notificationId = "NOT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        result.put("notificationId", notificationId);
        result.put("channels", new String[]{"SMS", "EMAIL"});
        result.put("language", preferredLanguage != null ? preferredLanguage : "EN");
        result.put("sentAt", LocalDateTime.now().toString());
        result.put("smsStatus", "DELIVERED");
        result.put("emailStatus", reporterEmail != null ? "SENT" : "NOT_AVAILABLE");

        log.info("Customer notification sent for FNOL: {} - ID: {}", fnolId, notificationId);
        return result;
    }

    /**
     * Sends document upload reminder to customer.
     */
    @JobWorker(type = "send-reminder-notification", autoComplete = true)
    public Map<String, Object> sendReminderNotification(
            final ActivatedJob job,
            @Variable String fnolId,
            @Variable String mobileNumber,
            @Variable String reminderType) {

        log.info("Sending {} reminder for FNOL: {}", reminderType, fnolId);

        Map<String, Object> result = new HashMap<>();

        String reminderId = "REM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        result.put("reminderId", reminderId);
        result.put("reminderType", reminderType);
        result.put("sentAt", LocalDateTime.now().toString());
        result.put("reminderStatus", "SENT");

        log.info("Reminder sent for FNOL: {} - ID: {}", fnolId, reminderId);
        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // ESCALATION WORKERS
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Escalates SLA breaches to appropriate manager.
     */
    @JobWorker(type = "escalate-to-manager", autoComplete = true)
    public Map<String, Object> escalateToManager(
            final ActivatedJob job,
            @Variable String fnolId,
            @Variable String escalationType,
            @Variable String severity) {

        log.warn("SLA ESCALATION for FNOL: {} - Type: {}, Severity: {}", fnolId, escalationType, severity);

        Map<String, Object> result = new HashMap<>();

        String escalationId = "ESC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String assignedManager = determineManager(severity);

        result.put("escalationId", escalationId);
        result.put("escalationType", escalationType);
        result.put("escalatedTo", assignedManager);
        result.put("escalatedAt", LocalDateTime.now().toString());
        result.put("escalationPriority", mapSeverityToPriority(severity));
        result.put("notificationSent", true);

        log.warn("Escalation created for FNOL: {} - ID: {}, Assigned to: {}",
                fnolId, escalationId, assignedManager);
        return result;
    }

    private String determineManager(String severity) {
        if ("HIGH".equals(severity)) return "senior.manager@insurance.com";
        if ("MEDIUM".equals(severity)) return "claims.supervisor@insurance.com";
        return "team.lead@insurance.com";
    }

    private String mapSeverityToPriority(String severity) {
        if ("HIGH".equals(severity)) return "P1 - Critical";
        if ("MEDIUM".equals(severity)) return "P2 - High";
        return "P3 - Medium";
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PAYMENT WORKERS
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Processes claim payment.
     * Uses country-specific currency based on FNOL country.
     * May throw PAYMENT_ERROR if payment fails.
     */
    @JobWorker(type = "process-payment", autoComplete = true)
    public Map<String, Object> processPayment(
            final ActivatedJob job,
            @Variable String fnolId,
            @Variable Double approvedAmount,
            @Variable String paymentMethod,
            @Variable String countryCode) {

        // Get country-specific currency
        String currency = getCurrencyForCountry(countryCode);
        log.info("Processing payment for FNOL: {} - Amount: {} {}", fnolId, approvedAmount, currency);

        Map<String, Object> result = new HashMap<>();

        String paymentReference = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        result.put("paymentReference", paymentReference);
        result.put("paymentAmount", approvedAmount);
        result.put("paymentCurrency", currency);
        result.put("paymentMethod", paymentMethod != null ? paymentMethod : "BANK_TRANSFER");
        result.put("paymentStatus", "COMPLETED");
        result.put("processedAt", LocalDateTime.now().toString());
        result.put("settlementDate", LocalDateTime.now().plusDays(3).toLocalDate().toString());

        log.info("Payment processed for FNOL: {} - Reference: {} - {} {}",
                fnolId, paymentReference, approvedAmount, currency);
        return result;
    }

    /**
     * Get currency code for a GCC country.
     *
     * @param countryCode ISO 2-letter country code
     * @return currency code (AED, SAR, KWD, QAR, BHD, OMR)
     */
    private String getCurrencyForCountry(String countryCode) {
        if (countryCode == null) return "AED"; // Default to AED
        return switch (countryCode.toUpperCase()) {
            case "AE" -> "AED";  // UAE Dirham
            case "SA" -> "SAR";  // Saudi Riyal
            case "KW" -> "KWD";  // Kuwaiti Dinar
            case "QA" -> "QAR";  // Qatari Riyal
            case "BH" -> "BHD";  // Bahraini Dinar
            case "OM" -> "OMR";  // Omani Rial
            default -> "AED";   // Default to AED
        };
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // POLICE REPORT WORKERS
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Requests police report from authorities.
     */
    @JobWorker(type = "request-police-report", autoComplete = true)
    public Map<String, Object> requestPoliceReport(
            final ActivatedJob job,
            @Variable String fnolId,
            @Variable String incidentLocation,
            @Variable String incidentDate) {

        log.info("Requesting police report for FNOL: {}", fnolId);

        Map<String, Object> result = new HashMap<>();

        String requestId = "PRQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        result.put("policeReportRequestId", requestId);
        result.put("requestedFrom", "GCC Police Department");
        result.put("requestedAt", LocalDateTime.now().toString());
        result.put("expectedResponseDays", 5);
        result.put("requestStatus", "SUBMITTED");

        log.info("Police report requested for FNOL: {} - Request ID: {}", fnolId, requestId);
        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // FRAUD DETECTION WORKERS
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Initializes fraud check process.
     */
    @JobWorker(type = "initialize-fraud-check", autoComplete = true)
    public Map<String, Object> initializeFraudCheck(
            final ActivatedJob job,
            @Variable String claimId) {

        log.info("Initializing fraud check for claim: {}", claimId);

        Map<String, Object> result = new HashMap<>();
        result.put("fraudCheckId", "FRD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        result.put("checkInitiatedAt", LocalDateTime.now().toString());
        result.put("checksToRun", new String[]{"velocity", "duplicate", "blacklist", "pattern"});

        return result;
    }

    /**
     * Performs velocity check (claim frequency analysis).
     */
    @JobWorker(type = "fraud-velocity-check", autoComplete = true)
    public Map<String, Object> fraudVelocityCheck(
            final ActivatedJob job,
            @Variable String mobileNumber,
            @Variable String nationalId) {

        log.info("Running velocity check for: {}", nationalId);

        Map<String, Object> result = new HashMap<>();

        // Simulate velocity check
        int claimsLast30Days = random.nextInt(3);
        int claimsLast90Days = random.nextInt(5);

        String riskLevel = "NONE";
        if (claimsLast30Days >= 3) riskLevel = "CRITICAL";
        else if (claimsLast30Days >= 2) riskLevel = "HIGH";
        else if (claimsLast90Days >= 3) riskLevel = "MEDIUM";
        else if (claimsLast30Days >= 1) riskLevel = "LOW";

        result.put("velocityRiskLevel", riskLevel);
        result.put("claimsLast30Days", claimsLast30Days);
        result.put("claimsLast90Days", claimsLast90Days);

        return result;
    }

    /**
     * Performs duplicate claim check.
     */
    @JobWorker(type = "fraud-duplicate-check", autoComplete = true)
    public Map<String, Object> fraudDuplicateCheck(
            final ActivatedJob job,
            @Variable String claimId,
            @Variable String location) {

        log.info("Running duplicate check for claim: {}", claimId);

        Map<String, Object> result = new HashMap<>();

        // Simulate duplicate check
        boolean hasDuplicate = random.nextDouble() < 0.1; // 10% chance of duplicate

        result.put("duplicateRiskLevel", hasDuplicate ? "HIGH" : "NONE");
        result.put("potentialDuplicates", hasDuplicate ? 1 : 0);
        result.put("similarityScore", hasDuplicate ? 0.92 : 0.0);

        return result;
    }

    /**
     * Performs blacklist check.
     */
    @JobWorker(type = "fraud-blacklist-check", autoComplete = true)
    public Map<String, Object> fraudBlacklistCheck(
            final ActivatedJob job,
            @Variable String nationalId,
            @Variable String mobileNumber) {

        log.info("Running blacklist check for: {}", nationalId);

        Map<String, Object> result = new HashMap<>();

        // Simulate blacklist check
        boolean isBlacklisted = random.nextDouble() < 0.02; // 2% chance

        result.put("blacklistMatch", isBlacklisted);
        result.put("blacklistSource", isBlacklisted ? "REGIONAL_FRAUD_DATABASE" : null);

        return result;
    }

    /**
     * Performs ML-based pattern analysis.
     */
    @JobWorker(type = "fraud-pattern-check", autoComplete = true)
    public Map<String, Object> fraudPatternCheck(
            final ActivatedJob job,
            @Variable String claimId,
            @Variable String description) {

        log.info("Running pattern analysis for claim: {}", claimId);

        Map<String, Object> result = new HashMap<>();

        // Simulate ML pattern score
        int patternScore = random.nextInt(50); // 0-50 for most cases

        result.put("patternScore", patternScore);
        result.put("patternFlags", patternScore > 30 ? new String[]{"UNUSUAL_TIME", "SUSPICIOUS_LOCATION"} : new String[]{});
        result.put("modelConfidence", 0.85 + random.nextDouble() * 0.1);

        return result;
    }

    /**
     * Collects all fraud indicators.
     */
    @JobWorker(type = "collect-fraud-indicators", autoComplete = true)
    public Map<String, Object> collectFraudIndicators(
            final ActivatedJob job,
            @Variable String velocityRiskLevel,
            @Variable String duplicateRiskLevel,
            @Variable Boolean blacklistMatch,
            @Variable Integer patternScore) {

        log.info("Collecting fraud indicators");

        Map<String, Object> result = new HashMap<>();

        StringBuilder indicators = new StringBuilder();
        if (!"NONE".equals(velocityRiskLevel)) indicators.append("Velocity: ").append(velocityRiskLevel).append("; ");
        if (!"NONE".equals(duplicateRiskLevel)) indicators.append("Duplicate: ").append(duplicateRiskLevel).append("; ");
        if (Boolean.TRUE.equals(blacklistMatch)) indicators.append("BLACKLIST MATCH; ");
        if (patternScore != null && patternScore > 25) indicators.append("Pattern Score: ").append(patternScore).append("; ");

        result.put("fraudIndicators", indicators.length() > 0 ? indicators.toString() : "No indicators");
        result.put("collectedAt", LocalDateTime.now().toString());

        return result;
    }

    /**
     * Finalizes fraud check output.
     */
    @JobWorker(type = "finalize-fraud-output", autoComplete = true)
    public Map<String, Object> finalizeFraudOutput(
            final ActivatedJob job,
            @Variable Integer fraudScoreComponent,
            @Variable Boolean requiresManualReview) {

        log.info("Finalizing fraud output - Score: {}", fraudScoreComponent);

        Map<String, Object> result = new HashMap<>();

        // Cap fraud score at 100
        int finalScore = Math.min(100, fraudScoreComponent != null ? fraudScoreComponent : 0);

        result.put("fraudScore", finalScore);
        result.put("requiresManualReview", finalScore >= 50 || Boolean.TRUE.equals(requiresManualReview));
        result.put("riskCategory", categorizeRisk(finalScore));
        result.put("completedAt", LocalDateTime.now().toString());

        return result;
    }

    private String categorizeRisk(int score) {
        if (score >= 80) return "CRITICAL";
        if (score >= 60) return "HIGH";
        if (score >= 40) return "MEDIUM";
        if (score >= 20) return "LOW";
        return "MINIMAL";
    }
}
