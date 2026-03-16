-- Food & Dining Module: Menu Categories, Menu Items, Tables, Sessions, Orders, and Recipes

-- Menu Categories
CREATE TABLE IF NOT EXISTS menu_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Menu Items
CREATE TABLE IF NOT EXISTS menu_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    name_zh VARCHAR(255),
    description TEXT,
    description_zh TEXT,
    price DECIMAL(19, 2),
    spice_level VARCHAR(50),
    allergens VARCHAR(255),
    is_signature BOOLEAN DEFAULT FALSE,
    prep_time_mins INT,
    sort_order INT,
    category_id BIGINT,
    available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_menu_item_category FOREIGN KEY (category_id) REFERENCES menu_categories(id)
);

-- Restaurant Tables
CREATE TABLE IF NOT EXISTS restaurant_tables (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_number VARCHAR(50) NOT NULL UNIQUE,
    capacity INT,
    location VARCHAR(100),
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Dining Sessions
CREATE TABLE IF NOT EXISTS dining_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_id BIGINT,
    stay_id BIGINT,
    guest_name VARCHAR(255),
    guest_phone VARCHAR(50),
    pax_count INT,
    split_count INT DEFAULT 1,
    billing_type VARCHAR(50),
    served_by BIGINT,
    opened_at DATETIME,
    closed_at DATETIME,
    status VARCHAR(50),
    total_amount DECIMAL(19, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_session_table FOREIGN KEY (table_id) REFERENCES restaurant_tables(id)
);

-- Dining Orders
CREATE TABLE IF NOT EXISTS dining_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT NOT NULL,
    menu_item_id BIGINT NOT NULL,
    quantity INT,
    unit_price DECIMAL(19, 2),
    discount_pct DECIMAL(5, 2),
    tax_pct DECIMAL(5, 2),
    total_amount DECIMAL(19, 2),
    notes TEXT,
    status VARCHAR(50),
    round_no INT DEFAULT 1,
    ordered_at DATETIME,
    served_at DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_session FOREIGN KEY (session_id) REFERENCES dining_sessions(id),
    CONSTRAINT fk_order_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- Recipe Items
CREATE TABLE IF NOT EXISTS recipe_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    menu_item_id BIGINT,
    ingredient_id BIGINT,
    quantity DECIMAL(19, 4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_recipe_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
    CONSTRAINT fk_recipe_ingredient FOREIGN KEY (ingredient_id) REFERENCES inventory_items(id)
);
