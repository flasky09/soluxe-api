CREATE TABLE IF NOT EXISTS inventory_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    description VARCHAR(255),
    active BOOLEAN DEFAULT TRUE,
    created_at DATETIME(6),
    updated_at DATETIME(6)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS inventory_units (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    description VARCHAR(255),
    active BOOLEAN DEFAULT TRUE,
    created_at DATETIME(6),
    updated_at DATETIME(6)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS inventory_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT,
    unit_id BIGINT,
    name VARCHAR(255) UNIQUE,
    buying_price DECIMAL(19, 2),
    current_stock DECIMAL(19, 2),
    minimum_stock DECIMAL(19, 2),
    notes VARCHAR(255),
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT fk_inventory_category FOREIGN KEY (category_id) REFERENCES inventory_categories(id),
    CONSTRAINT fk_inventory_unit FOREIGN KEY (unit_id) REFERENCES inventory_units(id)
) ENGINE=InnoDB;
