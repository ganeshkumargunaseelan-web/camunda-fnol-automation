-- ═══════════════════════════════════════════════════════════════════════════════
-- GCC MOTOR FNOL STARTER KIT - INITIAL DATABASE SCHEMA
-- ═══════════════════════════════════════════════════════════════════════════════
-- Version: 1.0.0
-- Copyright 2025 G. Ganesh Kumar
-- License: Apache License 2.0
-- ═══════════════════════════════════════════════════════════════════════════════

-- ─────────────────────────────────────────────────────────────────────────────────
-- FNOL ID SEQUENCE TABLE
-- ─────────────────────────────────────────────────────────────────────────────────
CREATE TABLE fnol_sequence (
    sequence_name VARCHAR(50) PRIMARY KEY,
    next_value BIGINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Insert default sequence
INSERT INTO fnol_sequence (sequence_name, next_value) VALUES ('FNOL', 1);

-- ─────────────────────────────────────────────────────────────────────────────────
-- IDEMPOTENCY TABLE
-- ─────────────────────────────────────────────────────────────────────────────────
CREATE TABLE idempotency_keys (
    id BIGSERIAL PRIMARY KEY,
    hashed_key VARCHAR(64) NOT NULL UNIQUE,
    fnol_id VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_idempotency_hashed_key ON idempotency_keys(hashed_key);
CREATE INDEX idx_idempotency_expires_at ON idempotency_keys(expires_at);

-- ─────────────────────────────────────────────────────────────────────────────────
-- FNOL CASE TABLE
-- ─────────────────────────────────────────────────────────────────────────────────
CREATE TABLE fnol_cases (
    id BIGSERIAL PRIMARY KEY,
    fnol_id VARCHAR(50) NOT NULL UNIQUE,

    -- Country & Contact
    country VARCHAR(10) NOT NULL,
    mobile_number VARCHAR(20) NOT NULL,
    national_id VARCHAR(50) NOT NULL,
    reporter_name VARCHAR(200),
    reporter_email VARCHAR(200),

    -- Vehicle Information
    plate_number VARCHAR(20) NOT NULL,
    plate_country VARCHAR(10) NOT NULL,
    vehicle_type VARCHAR(20) NOT NULL,
    vehicle_make VARCHAR(100),
    vehicle_model VARCHAR(100),
    vehicle_year INTEGER,
    vehicle_color VARCHAR(50),

    -- Policy Information
    policy_number VARCHAR(50),
    coverage_type VARCHAR(20) NOT NULL,
    fleet_flag BOOLEAN NOT NULL DEFAULT FALSE,

    -- Incident Details
    incident_date DATE NOT NULL,
    incident_time TIME,
    incident_location VARCHAR(500),
    incident_latitude DECIMAL(10, 8),
    incident_longitude DECIMAL(11, 8),
    description TEXT,

    -- Incident Assessment
    drivable BOOLEAN NOT NULL DEFAULT TRUE,
    injuries BOOLEAN NOT NULL DEFAULT FALSE,
    third_party_involved BOOLEAN NOT NULL DEFAULT FALSE,
    police_report_number VARCHAR(100),

    -- Preferences
    preferred_language VARCHAR(10) NOT NULL DEFAULT 'EN',

    -- Processing
    status VARCHAR(50) NOT NULL DEFAULT 'SUBMITTED',
    severity_level VARCHAR(20),
    route VARCHAR(50),
    process_instance_key VARCHAR(100),

    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_fnol_cases_fnol_id ON fnol_cases(fnol_id);
CREATE INDEX idx_fnol_cases_country ON fnol_cases(country);
CREATE INDEX idx_fnol_cases_status ON fnol_cases(status);
CREATE INDEX idx_fnol_cases_incident_date ON fnol_cases(incident_date);
CREATE INDEX idx_fnol_cases_created_at ON fnol_cases(created_at);
CREATE INDEX idx_fnol_cases_process_key ON fnol_cases(process_instance_key);

-- ─────────────────────────────────────────────────────────────────────────────────
-- ATTACHMENTS TABLE
-- ─────────────────────────────────────────────────────────────────────────────────
CREATE TABLE fnol_attachments (
    id BIGSERIAL PRIMARY KEY,
    fnol_case_id BIGINT NOT NULL REFERENCES fnol_cases(id) ON DELETE CASCADE,
    attachment_type VARCHAR(20) NOT NULL,
    url VARCHAR(2048) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_fnol_case FOREIGN KEY (fnol_case_id) REFERENCES fnol_cases(id)
);

CREATE INDEX idx_fnol_attachments_case_id ON fnol_attachments(fnol_case_id);

-- ─────────────────────────────────────────────────────────────────────────────────
-- WEBHOOK NOTIFICATIONS TABLE (for tracking/retry)
-- ─────────────────────────────────────────────────────────────────────────────────
CREATE TABLE webhook_notifications (
    id BIGSERIAL PRIMARY KEY,
    fnol_id VARCHAR(50) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    payload TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    attempts INTEGER NOT NULL DEFAULT 0,
    last_attempt_at TIMESTAMP,
    next_retry_at TIMESTAMP,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_webhook_status ON webhook_notifications(status);
CREATE INDEX idx_webhook_next_retry ON webhook_notifications(next_retry_at);
