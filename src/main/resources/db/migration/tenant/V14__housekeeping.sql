-- Module: Housekeeping

CREATE TABLE IF NOT EXISTS housekeeping_logs (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id         BIGINT NOT NULL,
    action          VARCHAR(50) NOT NULL,
    notes           TEXT,
    performed_by    BIGINT NOT NULL,
    performed_at    DATETIME(6) NOT NULL,
    created_at      DATETIME(6),
    updated_at      DATETIME(6)
);
