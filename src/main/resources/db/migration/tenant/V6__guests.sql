-- Module: Guests
-- identity_types is a lookup table referenced by guests and guest_documents

CREATE TABLE IF NOT EXISTS identity_types (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255),
    active      TINYINT(1) NOT NULL DEFAULT 1,
    created_at  DATETIME(6),
    updated_at  DATETIME(6)
);

CREATE TABLE IF NOT EXISTS guests (
    id                       BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name                VARCHAR(255),
    phone                    VARCHAR(255) UNIQUE,
    email                    VARCHAR(255) UNIQUE,
    nationality              VARCHAR(255),
    address                  VARCHAR(255),
    company_name             VARCHAR(255),
    date_of_birth            DATE,
    gender                   VARCHAR(20),
    id_type                  VARCHAR(50),
    id_number                VARCHAR(255) UNIQUE,
    preferences              TEXT,
    vehicle_registration     VARCHAR(255),
    emergency_contact_name   VARCHAR(255),
    emergency_contact_phone  VARCHAR(255),
    image_url                TEXT,
    voided                   TINYINT(1) NOT NULL DEFAULT 0,
    created_at               DATETIME(6),
    updated_at               DATETIME(6)
);

CREATE TABLE IF NOT EXISTS guest_documents (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    guest_id        BIGINT NOT NULL,
    document_type   VARCHAR(50),
    document_number VARCHAR(255),
    file_path       TEXT,
    expiry_date     DATE,
    file_name       VARCHAR(255),
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    CONSTRAINT fk_guest_doc_guest FOREIGN KEY (guest_id) REFERENCES guests (id)
);
