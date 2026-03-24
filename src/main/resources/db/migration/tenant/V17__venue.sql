-- Module: Venue
-- venues is standalone
-- venue_bookings depends on venues
-- venue_booking_charges depends on venue_bookings

CREATE TABLE IF NOT EXISTS venues (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255) UNIQUE,
    type            VARCHAR(255),
    capacity        INT,
    rate_per_hour   DECIMAL(19, 2),
    rate_per_day    DECIMAL(19, 2),
    description     VARCHAR(255),
    created_at      DATETIME(6),
    updated_at      DATETIME(6)
);

CREATE TABLE IF NOT EXISTS venue_bookings (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    venue_id        BIGINT,
    client_name     VARCHAR(255),
    client_name_zh  VARCHAR(255),
    client_phone    VARCHAR(255),
    client_company  VARCHAR(255),
    event_type      VARCHAR(50),
    date_in         DATE,
    date_out        DATE,
    start_time      TIME,
    end_time        TIME,
    expected_guests INT,
    setup_type      VARCHAR(50),
    deposit         DECIMAL(19, 2),
    deposit_paid    TINYINT(1) NOT NULL DEFAULT 0,
    total_amount    DECIMAL(19, 2),
    status          VARCHAR(50),
    notes           VARCHAR(255),
    created_by      BIGINT,
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    CONSTRAINT fk_venue_booking_venue FOREIGN KEY (venue_id) REFERENCES venues (id)
);

CREATE TABLE IF NOT EXISTS venue_booking_charges (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    venue_booking_id    BIGINT,
    description         TEXT,
    quantity            DECIMAL(19, 2),
    unit_price          DECIMAL(19, 2),
    total_amount        DECIMAL(19, 2),
    voided              TINYINT(1) NOT NULL DEFAULT 0,
    created_at          DATETIME(6),
    updated_at          DATETIME(6),
    CONSTRAINT fk_vbc_booking FOREIGN KEY (venue_booking_id) REFERENCES venue_bookings (id)
);
