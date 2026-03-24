-- Module: Food & Restaurant
-- restaurant_tables and menu_categories are lookups
-- menu_items depends on menu_categories
-- recipe_items depends on menu_items + inventory_items (V11)
-- dining_sessions depends on restaurant_tables + stays (V8) + users (V2)
-- dining_orders depends on dining_sessions + menu_items

CREATE TABLE IF NOT EXISTS restaurant_tables (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_name  VARCHAR(255),
    capacity    INT NOT NULL DEFAULT 0,
    location    VARCHAR(50),
    is_vip      TINYINT(1) NOT NULL DEFAULT 0,
    notes       VARCHAR(255),
    status      VARCHAR(50) DEFAULT 'AVAILABLE',
    created_at  DATETIME(6),
    updated_at  DATETIME(6)
);

CREATE TABLE IF NOT EXISTS menu_categories (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) UNIQUE,
    name_zh     VARCHAR(255),
    sort_order  INT,
    is_active   TINYINT(1) NOT NULL DEFAULT 1,
    created_at  DATETIME(6),
    updated_at  DATETIME(6)
);

CREATE TABLE IF NOT EXISTS menu_items (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255) UNIQUE,
    name_zh         VARCHAR(255),
    description     VARCHAR(255),
    description_zh  VARCHAR(255),
    price           DECIMAL(19, 2),
    spice_level     VARCHAR(50),
    allergens       TEXT,
    is_signature    TINYINT(1) NOT NULL DEFAULT 0,
    prep_time_mins  INT,
    sort_order      INT,
    category_id     BIGINT,
    available       TINYINT(1) NOT NULL DEFAULT 1,
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    CONSTRAINT fk_menu_item_category FOREIGN KEY (category_id) REFERENCES menu_categories (id)
);

CREATE TABLE IF NOT EXISTS recipe_items (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    menu_item_id    BIGINT,
    ingredient_id   BIGINT,
    quantity        DECIMAL(19, 2),
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    CONSTRAINT fk_recipe_menu_item  FOREIGN KEY (menu_item_id)  REFERENCES menu_items (id),
    CONSTRAINT fk_recipe_ingredient FOREIGN KEY (ingredient_id) REFERENCES inventory_items (id)
);

CREATE TABLE IF NOT EXISTS dining_sessions (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_id    BIGINT,
    stay_id     BIGINT,
    guest_name  VARCHAR(255),
    guest_phone VARCHAR(255),
    pax_count   INT,
    split_count INT DEFAULT 1,
    billing_type VARCHAR(50),
    served_by   BIGINT,
    opened_at   DATETIME(6),
    closed_at   DATETIME(6),
    status      VARCHAR(50),
    total_amount DECIMAL(19, 2),
    created_at  DATETIME(6),
    updated_at  DATETIME(6),
    CONSTRAINT fk_dining_table    FOREIGN KEY (table_id)  REFERENCES restaurant_tables (id),
    CONSTRAINT fk_dining_stay     FOREIGN KEY (stay_id)   REFERENCES stays (id),
    CONSTRAINT fk_dining_served   FOREIGN KEY (served_by) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS dining_orders (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id      BIGINT,
    menu_item_id    BIGINT,
    quantity        INT,
    unit_price      DECIMAL(19, 2),
    discount_pct    DECIMAL(19, 2),
    tax_pct         DECIMAL(19, 2),
    total_amount    DECIMAL(19, 2),
    notes           VARCHAR(255),
    status          VARCHAR(50),
    round_no        INT DEFAULT 1,
    ordered_at      DATETIME(6),
    served_at       DATETIME(6),
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    CONSTRAINT fk_order_session   FOREIGN KEY (session_id)   REFERENCES dining_sessions (id),
    CONSTRAINT fk_order_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_items (id)
);
