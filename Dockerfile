# ═══════════════════════════════════════════════════════════════════════════════
# GCC Motor FNOL Starter Kit - Dockerfile
# Author: G. Ganesh Kumar | Solution Architect
# Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
# ═══════════════════════════════════════════════════════════════════════════════
# Multi-stage build for optimized image size
# ═══════════════════════════════════════════════════════════════════════════════

# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (cached layer)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests -B

# Stage 2: Create runtime image
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Add non-root user
RUN addgroup -S fnol && adduser -S fnol -G fnol

# Copy JAR from builder
COPY --from=builder /app/target/*.jar app.jar

# Set ownership
RUN chown -R fnol:fnol /app

# Switch to non-root user
USER fnol

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget -q --spider http://localhost:8080/actuator/health || exit 1

# JVM options for containerized environment
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
