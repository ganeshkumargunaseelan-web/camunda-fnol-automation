/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.api.filter;

import io.camunda.community.fnol.gcc.motor.infrastructure.config.FnolProperties;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.MessageDigest;

@Component
@Order(2)
public class ApiKeyValidationFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyValidationFilter.class);

    private final FnolProperties fnolProperties;

    public ApiKeyValidationFilter(FnolProperties fnolProperties) {
        this.fnolProperties = fnolProperties;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        // Skip API key validation for non-API endpoints
        if (!path.startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }

        // Skip validation for health and info endpoints
        if (path.startsWith("/api/v1/info") || path.startsWith("/api/v1/countries")) {
            chain.doFilter(request, response);
            return;
        }

        // Check if API key validation is enabled
        if (!fnolProperties.security().apiKey().enabled()) {
            chain.doFilter(request, response);
            return;
        }

        String configuredKey = fnolProperties.security().apiKey().key();
        if (configuredKey == null || configuredKey.isBlank()) {
            log.warn("API key validation enabled but no key configured");
            chain.doFilter(request, response);
            return;
        }

        String headerName = fnolProperties.security().apiKey().headerName();
        String providedKey = httpRequest.getHeader(headerName);

        if (providedKey == null || providedKey.isBlank()) {
            log.warn("API request without API key from IP: {}", getClientIp(httpRequest));
            sendUnauthorizedResponse(httpResponse, "Missing API key");
            return;
        }

        // Use constant-time comparison to prevent timing attacks
        if (!constantTimeEquals(configuredKey, providedKey)) {
            log.warn("Invalid API key attempt from IP: {}", getClientIp(httpRequest));
            sendUnauthorizedResponse(httpResponse, "Invalid API key");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        return MessageDigest.isEqual(a.getBytes(), b.getBytes());
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(String.format(
                "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"%s\"}", message));
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
