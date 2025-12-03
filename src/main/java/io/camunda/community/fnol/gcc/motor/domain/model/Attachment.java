/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.domain.model;

import io.camunda.community.fnol.gcc.motor.domain.enums.AttachmentType;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public record Attachment(
        AttachmentType type,
        String url,
        String fileName,
        Long fileSizeBytes,
        String mimeType,
        String description,
        Instant uploadedAt
) {

    private static final Set<String> ALLOWED_SCHEMES = Set.of("http", "https");
    private static final Set<String> BLOCKED_HOSTS = Set.of(
            "localhost", "127.0.0.1", "0.0.0.0", "::1"
    );

    public Attachment {
        Objects.requireNonNull(type, "Attachment type cannot be null");
        Objects.requireNonNull(url, "Attachment URL cannot be null");
        if (url.isBlank()) {
            throw new IllegalArgumentException("Attachment URL cannot be blank");
        }
        if (url.length() > 2048) {
            throw new IllegalArgumentException("Attachment URL exceeds maximum length of 2048 characters");
        }
        validateUrl(url);
        if (uploadedAt == null) {
            uploadedAt = Instant.now();
        }
    }

    private static void validateUrl(String url) {
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();

            if (scheme == null || !ALLOWED_SCHEMES.contains(scheme.toLowerCase())) {
                throw new IllegalArgumentException(
                        "Attachment URL must use HTTP or HTTPS protocol");
            }

            String host = uri.getHost();
            if (host == null || host.isBlank()) {
                throw new IllegalArgumentException("Attachment URL must have a valid host");
            }

            // Block localhost and loopback addresses
            if (BLOCKED_HOSTS.contains(host.toLowerCase())) {
                throw new IllegalArgumentException(
                        "Attachment URL cannot reference localhost or loopback addresses");
            }

            // Block private IP ranges (SSRF prevention)
            if (isPrivateIpRange(host)) {
                throw new IllegalArgumentException(
                        "Attachment URL cannot reference private network addresses");
            }

        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid attachment URL format: " + e.getMessage());
        }
    }

    private static boolean isPrivateIpRange(String host) {
        // Check for private IPv4 ranges: 10.x.x.x, 172.16-31.x.x, 192.168.x.x
        if (host.matches("^10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")) {
            return true;
        }
        if (host.matches("^172\\.(1[6-9]|2[0-9]|3[0-1])\\.\\d{1,3}\\.\\d{1,3}$")) {
            return true;
        }
        if (host.matches("^192\\.168\\.\\d{1,3}\\.\\d{1,3}$")) {
            return true;
        }
        // Check for link-local: 169.254.x.x
        if (host.matches("^169\\.254\\.\\d{1,3}\\.\\d{1,3}$")) {
            return true;
        }
        return false;
    }

    /**
     * Builder for creating Attachment instances.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Create a simple image attachment.
     *
     * @param url      the image URL
     * @param fileName the file name
     * @return Attachment instance
     */
    public static Attachment image(String url, String fileName) {
        return builder()
                .type(AttachmentType.IMAGE)
                .url(url)
                .fileName(fileName)
                .build();
    }

    /**
     * Create a simple document attachment.
     *
     * @param url      the document URL
     * @param fileName the file name
     * @return Attachment instance
     */
    public static Attachment document(String url, String fileName) {
        return builder()
                .type(AttachmentType.DOCUMENT)
                .url(url)
                .fileName(fileName)
                .build();
    }

    /**
     * Create a simple video attachment.
     *
     * @param url      the video URL
     * @param fileName the file name
     * @return Attachment instance
     */
    public static Attachment video(String url, String fileName) {
        return builder()
                .type(AttachmentType.VIDEO)
                .url(url)
                .fileName(fileName)
                .build();
    }

    /**
     * Check if this attachment is an image.
     *
     * @return true if image type
     */
    public boolean isImage() {
        return type == AttachmentType.IMAGE;
    }

    /**
     * Check if this attachment is a video.
     *
     * @return true if video type
     */
    public boolean isVideo() {
        return type == AttachmentType.VIDEO;
    }

    /**
     * Check if this attachment is a document.
     *
     * @return true if document type
     */
    public boolean isDocument() {
        return type == AttachmentType.DOCUMENT;
    }

    /**
     * Get a human-readable file size.
     *
     * @return formatted file size (e.g., "1.5 MB")
     */
    public String getFormattedFileSize() {
        if (fileSizeBytes == null || fileSizeBytes <= 0) {
            return "Unknown";
        }

        if (fileSizeBytes < 1024) {
            return fileSizeBytes + " B";
        } else if (fileSizeBytes < 1024 * 1024) {
            return String.format("%.1f KB", fileSizeBytes / 1024.0);
        } else if (fileSizeBytes < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", fileSizeBytes / (1024.0 * 1024));
        } else {
            return String.format("%.1f GB", fileSizeBytes / (1024.0 * 1024 * 1024));
        }
    }

    /**
     * Builder class for Attachment.
     */
    public static class Builder {
        private AttachmentType type;
        private String url;
        private String fileName;
        private Long fileSizeBytes;
        private String mimeType;
        private String description;
        private Instant uploadedAt;

        public Builder type(AttachmentType type) {
            this.type = type;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder fileSizeBytes(Long fileSizeBytes) {
            this.fileSizeBytes = fileSizeBytes;
            return this;
        }

        public Builder mimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder uploadedAt(Instant uploadedAt) {
            this.uploadedAt = uploadedAt;
            return this;
        }

        public Attachment build() {
            return new Attachment(type, url, fileName, fileSizeBytes, mimeType, description, uploadedAt);
        }
    }
}
