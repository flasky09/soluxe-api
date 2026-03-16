CREATE TABLE IF NOT EXISTS hotels (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    address VARCHAR(255),
    phone VARCHAR(255),
    email VARCHAR(255),
    created_at DATETIME(6),
    updated_at DATETIME(6)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS hotel_settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    allow_walk_in BOOLEAN NOT NULL,
    allow_complimentary BOOLEAN NOT NULL,
    kra_pin VARCHAR(255),
    vat_status VARCHAR(255),
    company_reg VARCHAR(255),
    currency VARCHAR(255),
    website VARCHAR(255),
    check_in_time VARCHAR(255),
    check_out_time VARCHAR(255),
    vat_percentage DECIMAL(19, 2),
    service_charge_percentage DECIMAL(19, 2),
    tourism_levy_percentage DECIMAL(19, 2),
    created_at DATETIME(6),
    updated_at DATETIME(6)
) ENGINE=InnoDB;
