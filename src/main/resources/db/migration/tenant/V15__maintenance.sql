-- Module: Maintenance
-- maintenance_issue_types is a lookup referenced by maintenance_tickets

CREATE TABLE IF NOT EXISTS maintenance_issue_types (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) UNIQUE,
    active      TINYINT(1) NOT NULL DEFAULT 1,
    created_at  DATETIME(6),
    updated_at  DATETIME(6)
);

CREATE TABLE IF NOT EXISTS maintenance_tickets (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id             BIGINT,
    reported_by         BIGINT NOT NULL,
    assigned_to         BIGINT,
    issue_type_id       BIGINT,
    description         TEXT NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'OPEN',
    priority            VARCHAR(50) NOT NULL DEFAULT 'MEDIUM',
    resolved_at         DATETIME(6),
    resolution_notes    TEXT,
    created_at          DATETIME(6),
    updated_at          DATETIME(6),
    CONSTRAINT fk_maint_issue_type FOREIGN KEY (issue_type_id) REFERENCES maintenance_issue_types (id)
);
