-- Accounts & Folio Module: Charge Types, Folios, Folio Charges, Cash Movements, Petty Cash, and Expenses

-- Charge Types
CREATE TABLE IF NOT EXISTS charge_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Folios
CREATE TABLE IF NOT EXISTS folios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    stay_id BIGINT,
    reservation_id BIGINT,
    venue_booking_id BIGINT,
    dining_session_id BIGINT,
    folio_type VARCHAR(50),
    status VARCHAR(50),
    opened_at DATETIME,
    closed_at DATETIME,
    total_amount DECIMAL(19, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Folio Charges
CREATE TABLE IF NOT EXISTS folio_charges (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    folio_id BIGINT NOT NULL,
    charge_type_id BIGINT,
    description TEXT,
    quantity DECIMAL(19, 4),
    unit_price DECIMAL(19, 2),
    discount_pct DECIMAL(5, 2) DEFAULT 0.00,
    tax_pct DECIMAL(5, 2) DEFAULT 0.00,
    total_amount DECIMAL(19, 2),
    charged_at DATETIME,
    added_by BIGINT,
    reference_id VARCHAR(255),
    voided BOOLEAN DEFAULT FALSE,
    void_reason TEXT,
    voided_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_charge_folio FOREIGN KEY (folio_id) REFERENCES folios(id),
    CONSTRAINT fk_charge_type FOREIGN KEY (charge_type_id) REFERENCES charge_types(id)
);

-- Cash Movements
CREATE TABLE IF NOT EXISTS cash_movements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    movement_date DATE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Petty Cash
CREATE TABLE IF NOT EXISTS petty_cash (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(19, 2) NOT NULL,
    expense_date DATE NOT NULL,
    description TEXT NOT NULL,
    issued_to VARCHAR(255),
    category VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Expense Types
CREATE TABLE IF NOT EXISTS expense_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    is_asset BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Expenses
CREATE TABLE IF NOT EXISTS expenses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description TEXT,
    amount DECIMAL(19, 2),
    expense_date DATE,
    expense_type_id BIGINT,
    payment_method VARCHAR(50),
    reference_number VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_expense_type FOREIGN KEY (expense_type_id) REFERENCES expense_types(id)
);

-- Payment Methods
CREATE TABLE IF NOT EXISTS payment_methods (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Folio Payments
CREATE TABLE IF NOT EXISTS folio_payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    folio_id BIGINT NOT NULL,
    payment_method_id BIGINT NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    reference_number VARCHAR(255),
    recorded_by BIGINT,
    recorded_at DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_folio FOREIGN KEY (folio_id) REFERENCES folios(id),
    CONSTRAINT fk_payment_method FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id)
);
