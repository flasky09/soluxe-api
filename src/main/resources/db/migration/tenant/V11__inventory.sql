-- Module: Inventory
-- inventory_units and inventory_categories are lookups
-- inventory_items depends on both
-- suppliers is standalone
-- purchase_orders depends on suppliers
-- purchase_order_items depends on purchase_orders + inventory_items

CREATE TABLE IF NOT EXISTS inventory_units (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) UNIQUE,
    description VARCHAR(255),
    active      TINYINT(1) NOT NULL DEFAULT 1,
    created_at  DATETIME(6),
    updated_at  DATETIME(6)
);

CREATE TABLE IF NOT EXISTS inventory_categories (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) UNIQUE,
    description VARCHAR(255),
    active      TINYINT(1) NOT NULL DEFAULT 1,
    created_at  DATETIME(6),
    updated_at  DATETIME(6)
);

CREATE TABLE IF NOT EXISTS suppliers (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255) NOT NULL UNIQUE,
    contact_person  VARCHAR(255),
    category        VARCHAR(50),
    phone           VARCHAR(255),
    email           VARCHAR(255) NOT NULL UNIQUE,
    address         VARCHAR(255),
    payment_terms   VARCHAR(255),
    notes           VARCHAR(255),
    is_active       TINYINT(1) NOT NULL DEFAULT 1,
    created_at      DATETIME(6),
    updated_at      DATETIME(6)
);

CREATE TABLE IF NOT EXISTS inventory_items (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id     BIGINT,
    name            VARCHAR(255) UNIQUE,
    buying_price    DECIMAL(19, 2),
    current_stock   DECIMAL(19, 2),
    minimum_stock   DECIMAL(19, 2),
    unit_id         BIGINT,
    notes           VARCHAR(255),
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    CONSTRAINT fk_inv_item_category FOREIGN KEY (category_id) REFERENCES inventory_categories (id),
    CONSTRAINT fk_inv_item_unit     FOREIGN KEY (unit_id)     REFERENCES inventory_units (id)
);

CREATE TABLE IF NOT EXISTS purchase_orders (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_id     BIGINT,
    order_date      DATE,
    expected_date   DATE,
    status          VARCHAR(50),
    notes           VARCHAR(255),
    created_by      BIGINT,
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    CONSTRAINT fk_po_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers (id)
);

CREATE TABLE IF NOT EXISTS purchase_order_items (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    purchase_order_id   BIGINT,
    inventory_item_id   BIGINT,
    quantity_ordered    DECIMAL(19, 2),
    quantity_received   DECIMAL(19, 2),
    unit_price          DECIMAL(19, 2),
    created_at          DATETIME(6),
    updated_at          DATETIME(6),
    CONSTRAINT fk_poi_order FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders (id),
    CONSTRAINT fk_poi_item  FOREIGN KEY (inventory_item_id) REFERENCES inventory_items (id)
);
