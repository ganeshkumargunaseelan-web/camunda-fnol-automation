/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.infrastructure.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.community.fnol.gcc.motor.application.port.out.WebhookNotifierPort;
import io.camunda.community.fnol.gcc.motor.domain.model.MotorFnolCase;
import io.camunda.community.fnol.gcc.motor.infrastructure.config.FnolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Map;

@Component
public class WebhookNotifierAdapter implements WebhookNotifierPort {

    private static final Logger log = LoggerFactory.getLogger(WebhookNotifierAdapter.class);

    private final FnolProperties fnolProperties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public WebhookNotifierAdapter(FnolProperties fnolProperties, ObjectMapper objectMapper) {
        this.fnolProperties = fnolProperties;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(fnolProperties.webhook().timeoutMs()))
                .build();
    }

    @Override
    @Async
    public void notifyFnolCreated(MotorFnolCase fnolCase) {
        if (!fnolProperties.webhook().enabled()) {
            log.debug("Webhook notifications disabled");
            return;
        }

        String webhookUrl = fnolProperties.webhook().url();
        if (webhookUrl == null || webhookUrl.isBlank()) {
            log.debug("No webhook URL configured");
            return;
        }

        try {
            Map<String, Object> payload = buildPayload(fnolCase, "FNOL_CREATED");
            sendWebhook(webhookUrl, payload);
        } catch (Exception e) {
            log.error("Failed to send webhook notification for FNOL: {}", fnolCase.getFnolId(), e);
        }
    }

    @Override
    @Async
    public void notifyFnolUpdated(MotorFnolCase fnolCase) {
        if (!fnolProperties.webhook().enabled()) {
            return;
        }

        String webhookUrl = fnolProperties.webhook().url();
        if (webhookUrl == null || webhookUrl.isBlank()) {
            return;
        }

        try {
            Map<String, Object> payload = buildPayload(fnolCase, "FNOL_UPDATED");
            sendWebhook(webhookUrl, payload);
        } catch (Exception e) {
            log.error("Failed to send update webhook for FNOL: {}", fnolCase.getFnolId(), e);
        }
    }

    /**
     * Build the webhook payload.
     */
    private Map<String, Object> buildPayload(MotorFnolCase fnolCase, String eventType) {
        return Map.of(
                "eventType", eventType,
                "timestamp", LocalDateTime.now().toString(),
                "fnolId", fnolCase.getFnolId() != null ? fnolCase.getFnolId() : "",
                "country", fnolCase.getCountry() != null ? fnolCase.getCountry().name() : "",
                "status", fnolCase.getProcessStatus() != null ? fnolCase.getProcessStatus() : "",
                "severityLevel", fnolCase.getSeverityLevel() != null ? fnolCase.getSeverityLevel() : "",
                "route", fnolCase.getRoute() != null ? fnolCase.getRoute() : "",
                "processInstanceKey", fnolCase.getProcessInstanceKey() != null ? fnolCase.getProcessInstanceKey() : ""
        );
    }

    /**
     * Send the webhook HTTP request.
     */
    private void sendWebhook(String url, Map<String, Object> payload) throws Exception {
        String jsonPayload = objectMapper.writeValueAsString(payload);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMillis(fnolProperties.webhook().timeoutMs()))
                .header("Content-Type", "application/json")
                .header("User-Agent", "GCC-Motor-FNOL-Webhook/1.0")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload));

        // Add signature if secret is configured
        String secret = fnolProperties.webhook().secret();
        if (secret != null && !secret.isBlank()) {
            String signature = computeSignature(jsonPayload, secret);
            requestBuilder.header("X-Webhook-Signature", signature);
        }

        HttpRequest request = requestBuilder.build();

        int retryCount = fnolProperties.webhook().retryCount();
        int attempt = 0;
        Exception lastException = null;

        while (attempt <= retryCount) {
            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    log.info("Webhook sent successfully to {}. Status: {}", url, response.statusCode());
                    return;
                } else {
                    log.warn("Webhook returned non-success status: {}. Body: {}",
                            response.statusCode(), response.body());
                }
            } catch (Exception e) {
                lastException = e;
                log.warn("Webhook attempt {} failed: {}", attempt + 1, e.getMessage());
            }

            attempt++;
            if (attempt <= retryCount) {
                // Exponential backoff
                Thread.sleep((long) Math.pow(2, attempt) * 1000);
            }
        }

        if (lastException != null) {
            throw lastException;
        }
    }

    /**
     * Compute HMAC-SHA256 signature for webhook payload.
     */
    private String computeSignature(String payload, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return "sha256=" + HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            log.error("Failed to compute webhook signature", e);
            return "";
        }
    }
}
