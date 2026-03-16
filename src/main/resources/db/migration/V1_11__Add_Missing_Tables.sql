-- Migration V1_11: Add missing tables and fix schema mismatches

-- 1. Missing Guest related tables
CREATE TABLE IF NOT EXISTS guest_documents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    guest_id BIGINT NOT NULL,
    document_type VARCHAR(50),
    document_number VARCHAR(100),
    file_path VARCHAR(512),
    expiry_date DATE,
    file_name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_document_guest FOREIGN KEY (guest_id) REFERENCES guests(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS identity_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. Missing Room related tables
CREATE TABLE IF NOT EXISTS room_price_plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    name_zh VARCHAR(255),
    description TEXT,
    description_zh TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 3. Missing Inventory related tables
CREATE TABLE IF NOT EXISTS suppliers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    contact_person VARCHAR(255),
    category VARCHAR(50),
    phone VARCHAR(50),
    email VARCHAR(255) NOT NULL UNIQUE,
    address TEXT,
    payment_terms TEXT,
    notes TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS purchase_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_id BIGINT,
    order_date DATE,
    expected_date DATE,
    status VARCHAR(50),
    notes TEXT,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_po_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS purchase_order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    purchase_order_id BIGINT,
    inventory_item_id BIGINT,
    quantity_ordered DECIMAL(19, 2),
    quantity_received DECIMAL(19, 2),
    unit_price DECIMAL(19, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_poi_po FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders(id),
    CONSTRAINT fk_poi_item FOREIGN KEY (inventory_item_id) REFERENCES inventory_items(id)
) ENGINE=InnoDB;

-- 4. Missing Stay related tables
CREATE TABLE IF NOT EXISTS stay_additional_guests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    stay_id BIGINT NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    id_type VARCHAR(50),
    id_number VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_additional_guest_stay FOREIGN KEY (stay_id) REFERENCES stays(id)
) ENGINE=InnoDB;

-- 5. Fix mismatches in existing tables from V1_9
-- Split into separate statements for H2 compatibility
ALTER TABLE venue_booking_charges ADD COLUMN quantity DECIMAL(19, 4);
ALTER TABLE venue_booking_charges ADD COLUMN unit_price DECIMAL(19, 2);
ALTER TABLE venue_booking_charges ADD COLUMN total_amount DECIMAL(19, 2);
ALTER TABLE venue_booking_charges ADD COLUMN voided BOOLEAN DEFAULT FALSE;

ALTER TABLE maintenance_issue_types ADD COLUMN active BOOLEAN DEFAULT TRUE;
