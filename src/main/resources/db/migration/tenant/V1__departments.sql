-- Module: Employee / Departments
-- Referenced by: users, employees

CREATE TABLE IF NOT EXISTS departments (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255),
    active      TINYINT(1)   NOT NULL DEFAULT 1,
    created_at  DATETIME(6),
    updated_at  DATETIME(6)
);
