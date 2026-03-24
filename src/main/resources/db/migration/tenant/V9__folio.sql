-- Module: Folio
-- charge_types and payment_methods are lookups; folios depends on nothing;
-- folio_charges depends on folios + charge_types; folio_payments and folio_receipts depend on folios

CREATE TABLE IF NOT EXISTS charge_types (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) UNIQUE,
    active      TINYINT(1) NOT NULL DEFAULT 1,
    created_at  DATETIME(6),
    updated_at  DATETIME(6)
);

CREATE TABLE IF NOT EXISTS payment_methods (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) UNIQUE,
    description VARCHAR(255),
    active      TINYINT(1) NOT NULL DEFAULT 1,
    created_at  DATETIME(6),
    updated_at  DATETIME(6)
);

CREATE TABLE IF NOT EXISTS folios (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    stay_id             BIGINT,
    reservation_id      BIGINT,
    venue_booking_id    BIGINT,
    dining_session_id   BIGINT,
    version             BIGINT DEFAULT 0,
    folio_type          VARCHAR(50),
    status              VARCHAR(50),
    opened_at           DATETIME(6),
    closed_at           DATETIME(6),
    total_amount        DECIMAL(19, 2) DEFAULT 0.00,
    created_at          DATETIME(6),
    updated_at          DATETIME(6)
);

CREATE TABLE IF NOT EXISTS folio_charges (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    folio_id        BIGINT NOT NULL,
    charge_type_id  BIGINT,
    description     VARCHAR(255),
    quantity        DECIMAL(19, 2),
    unit_price      DECIMAL(19, 2),
    discount_pct    DECIMAL(19, 2) DEFAULT 0.00,
    tax_pct         DECIMAL(19, 2) DEFAULT 0.00,
    total_amount    DECIMAL(19, 2),
    charged_at      DATETIME(6),
    added_by        BIGINT,
    reference_id    VARCHAR(255),
    period_start    DATETIME(6),
    period_end      DATETIME(6),
    voided          TINYINT(1) NOT NULL DEFAULT 0,
    void_reason     VARCHAR(255),
    voided_by       BIGINT,
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    CONSTRAINT fk_folio_charge_type FOREIGN KEY (charge_type_id) REFERENCES charge_types (id)
);

CREATE TABLE IF NOT EXISTS folio_payments (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    folio_id            BIGINT,
    payment_method_id   BIGINT,
    amount              DECIMAL(19, 2),
    reference_number    VARCHAR(255),
    notes               VARCHAR(255),
    recorded_by         BIGINT,
    recorded_at         DATETIME(6),
    created_at          DATETIME(6),
    updated_at          DATETIME(6)
);

CREATE TABLE IF NOT EXISTS folio_receipts (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    receipt_number  VARCHAR(255) NOT NULL UNIQUE,
    payment_id      BIGINT NOT NULL,
    folio_id        BIGINT NOT NULL,
    amount          DECIMAL(19, 2) NOT NULL,
    issued_at       DATETIME(6) NOT NULL,
    issued_by       BIGINT NOT NULL,
    created_at      DATETIME(6),
    updated_at      DATETIME(6)
);
