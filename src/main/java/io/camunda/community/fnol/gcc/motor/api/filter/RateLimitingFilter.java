/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.api.filter;

import io.camunda.community.fnol.gcc.motor.infrastructure.config.FnolProperties;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
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
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(3)
public class RateLimitingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitingFilter.class);

    private final FnolProperties fnolProperties;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public RateLimitingFilter(FnolProperties fnolProperties) {
        this.fnolProperties = fnolProperties;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        // Skip rate limiting for non-API endpoints
        if (!path.startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }

        // Check if rate limiting is enabled
        if (!fnolProperties.security().rateLimit().enabled()) {
            chain.doFilter(request, response);
            return;
        }

        String clientId = getClientIdentifier(httpRequest);
        Bucket bucket = buckets.computeIfAbsent(clientId, this::createBucket);

        long availableTokens = bucket.getAvailableTokens();
        httpResponse.setHeader("X-RateLimit-Remaining", String.valueOf(availableTokens));

        if (!bucket.tryConsume(1)) {
            log.warn("Rate limit exceeded for client: {}", clientId);
            sendRateLimitExceededResponse(httpResponse);
            return;
        }

        chain.doFilter(request, response);
    }

    private Bucket createBucket(String clientId) {
        int requestsPerMinute = fnolProperties.security().rateLimit().requestsPerMinute();

        Bandwidth limit = Bandwidth.builder()
                .capacity(requestsPerMinute)
                .refillGreedy(requestsPerMinute, Duration.ofMinutes(1))
                .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    private String getClientIdentifier(HttpServletRequest request) {
        // Use API key if present, otherwise use IP
        String apiKeyHeader = fnolProperties.security().apiKey().headerName();
        String apiKey = request.getHeader(apiKeyHeader);

        if (apiKey != null && !apiKey.isBlank()) {
            // Hash the API key to avoid storing it in memory
            return "key:" + apiKey.hashCode();
        }

        return "ip:" + getClientIp(request);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private void sendRateLimitExceededResponse(HttpServletResponse response) throws IOException {
        response.setStatus(429);
        response.setContentType("application/json");
        response.setHeader("Retry-After", "60");
        response.getWriter().write(
                "{\"status\":429,\"error\":\"Too Many Requests\",\"message\":\"Rate limit exceeded. Please retry after 60 seconds.\"}");
    }
}
