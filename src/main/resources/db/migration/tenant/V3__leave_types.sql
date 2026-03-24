-- Module: Employee / Leave
-- Referenced by: leave_requests (V10)

CREATE TABLE IF NOT EXISTS leave_types (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) UNIQUE,
    active      TINYINT(1) NOT NULL DEFAULT 1,
    created_at  DATETIME(6),
    updated_at  DATETIME(6)
);
