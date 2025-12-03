/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
package io.camunda.community.fnol.gcc.motor.domain.enums;

import java.util.Optional;
import java.util.Set;

public enum AttachmentType {

    /**
     * Image files (photos of damage, scene, etc.)
     */
    IMAGE("Image", Set.of("jpg", "jpeg", "png", "gif", "webp", "heic")),

    /**
     * Video files (dashcam footage, scene video)
     */
    VIDEO("Video", Set.of("mp4", "mov", "avi", "mkv", "webm")),

    /**
     * Document files (police report, license, registration)
     */
    DOCUMENT("Document", Set.of("pdf", "doc", "docx", "xls", "xlsx")),

    /**
     * Audio files (voice recordings, witness statements)
     */
    AUDIO("Audio", Set.of("mp3", "wav", "m4a", "ogg", "aac"));

    private final String displayName;
    private final Set<String> allowedExtensions;

    AttachmentType(String displayName, Set<String> allowedExtensions) {
        this.displayName = displayName;
        this.allowedExtensions = allowedExtensions;
    }

    /**
     * Get the display name for UI presentation.
     *
     * @return display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get allowed file extensions for this type.
     *
     * @return set of allowed extensions (lowercase, without dot)
     */
    public Set<String> getAllowedExtensions() {
        return allowedExtensions;
    }

    /**
     * Check if a file extension is valid for this attachment type.
     *
     * @param extension file extension (with or without dot)
     * @return true if valid
     */
    public boolean isValidExtension(String extension) {
        if (extension == null || extension.isBlank()) {
            return false;
        }
        String ext = extension.toLowerCase().trim();
        if (ext.startsWith(".")) {
            ext = ext.substring(1);
        }
        return allowedExtensions.contains(ext);
    }

    /**
     * Find attachment type from string value.
     *
     * @param value the value to parse
     * @return Optional containing attachment type if found
     */
    public static Optional<AttachmentType> fromValue(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(valueOf(value.toUpperCase().trim()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * Detect attachment type from file extension.
     *
     * @param fileName file name or extension
     * @return Optional containing detected type
     */
    public static Optional<AttachmentType> fromFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return Optional.empty();
        }
        String ext = fileName.toLowerCase().trim();
        int lastDot = ext.lastIndexOf('.');
        if (lastDot > 0) {
            ext = ext.substring(lastDot + 1);
        }

        for (AttachmentType type : values()) {
            if (type.allowedExtensions.contains(ext)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
