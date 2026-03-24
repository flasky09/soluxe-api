-- Module: Users
-- Depends on: departments (V1)

CREATE TABLE IF NOT EXISTS users (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(255) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    full_name       VARCHAR(255),
    phone_number    VARCHAR(255),
    email           VARCHAR(255) NOT NULL UNIQUE,
    is_active       TINYINT(1)   NOT NULL DEFAULT 1,
    department_id   BIGINT,
    role            VARCHAR(50),
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    CONSTRAINT fk_users_department FOREIGN KEY (department_id) REFERENCES departments (id)
);
