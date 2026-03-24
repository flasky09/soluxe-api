-- Module: Stays

CREATE TABLE IF NOT EXISTS stays (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_id      BIGINT,
    guest_id            BIGINT NOT NULL,
    room_id             BIGINT NOT NULL,
    plan_code           VARCHAR(50),
    rate_per_night      DECIMAL(19, 2),
    date_in             DATETIME(6) NOT NULL,
    date_out            DATETIME(6),
    actual_date_out     DATETIME(6),
    adults              INT,
    children            INT,
    is_complimentary    TINYINT(1) DEFAULT 0,
    business_source     VARCHAR(50),
    arriving_from       VARCHAR(255),
    next_destination    VARCHAR(255),
    arrival_flight_no   VARCHAR(255),
    departure_flight_no VARCHAR(255),
    card_encoded        TINYINT(1) DEFAULT 0,
    checked_in_by       BIGINT,
    checked_out_by      BIGINT,
    status              VARCHAR(50),
    notes               TEXT,
    created_at          DATETIME(6),
    updated_at          DATETIME(6)
);

CREATE TABLE IF NOT EXISTS stay_additional_guests (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    stay_id     BIGINT NOT NULL,
    full_name   VARCHAR(255) NOT NULL,
    id_type     VARCHAR(50),
    id_number   VARCHAR(255),
    created_at  DATETIME(6),
    updated_at  DATETIME(6)
);
