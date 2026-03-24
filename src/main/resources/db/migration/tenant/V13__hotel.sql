-- Module: Hotel Profile & Settings

CREATE TABLE IF NOT EXISTS hotels (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255),
    address     VARCHAR(255),
    phone       VARCHAR(255),
    email       VARCHAR(255),
    created_at  DATETIME(6),
    updated_at  DATETIME(6)
);

CREATE TABLE IF NOT EXISTS hotel_settings (
    id                          BIGINT AUTO_INCREMENT PRIMARY KEY,
    allow_walk_in               TINYINT(1) NOT NULL DEFAULT 1,
    allow_complimentary         TINYINT(1) NOT NULL DEFAULT 0,
    kra_pin                     VARCHAR(255),
    vat_status                  VARCHAR(255),
    company_reg                 VARCHAR(255),
    website                     VARCHAR(255),
    check_in_time               VARCHAR(10),
    check_out_time              VARCHAR(10),
    vat_percentage              DECIMAL(19, 2),
    service_charge_percentage   DECIMAL(19, 2),
    tourism_levy_percentage     DECIMAL(19, 2),
    created_at                  DATETIME(6),
    updated_at                  DATETIME(6)
);
