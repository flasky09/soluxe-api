-- Module: Finance
-- expense_types is a lookup; expenses depends on it
-- petty_cash and cash_movements are standalone

CREATE TABLE IF NOT EXISTS expense_types (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) UNIQUE,
    is_asset    TINYINT(1) NOT NULL DEFAULT 0,
    created_at  DATETIME(6),
    updated_at  DATETIME(6)
);

CREATE TABLE IF NOT EXISTS expenses (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    description         TEXT,
    amount              DECIMAL(19, 2),
    expense_date        DATE,
    expense_type_id     BIGINT,
    payment_method      VARCHAR(255),
    reference_number    VARCHAR(255),
    created_at          DATETIME(6),
    updated_at          DATETIME(6),
    CONSTRAINT fk_expense_type FOREIGN KEY (expense_type_id) REFERENCES expense_types (id)
);

CREATE TABLE IF NOT EXISTS petty_cash (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount          DECIMAL(19, 2) NOT NULL,
    expense_date    DATE NOT NULL,
    description     TEXT NOT NULL,
    issued_to       VARCHAR(255),
    category        VARCHAR(255),
    created_at      DATETIME(6),
    updated_at      DATETIME(6)
);

CREATE TABLE IF NOT EXISTS cash_movements (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    type            VARCHAR(50) NOT NULL,
    amount          DECIMAL(19, 2) NOT NULL,
    movement_date   DATE NOT NULL,
    description     VARCHAR(255),
    created_at      DATETIME(6),
    updated_at      DATETIME(6)
);
