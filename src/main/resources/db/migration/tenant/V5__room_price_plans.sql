-- Module: Rooms / Price Plans

CREATE TABLE IF NOT EXISTS room_price_plans (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255) UNIQUE,
    name_zh         VARCHAR(255),
    description     VARCHAR(255),
    description_zh  VARCHAR(255),
    is_active       TINYINT(1) NOT NULL DEFAULT 1,
    created_at      DATETIME(6),
    updated_at      DATETIME(6)
);
