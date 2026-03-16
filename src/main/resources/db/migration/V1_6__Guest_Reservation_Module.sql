-- Guest & Reservation Module: Guests, Reservations, and Stays

-- Guests
CREATE TABLE IF NOT EXISTS guests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(50) UNIQUE,
    email VARCHAR(255) UNIQUE,
    nationality VARCHAR(100),
    address TEXT,
    company_name VARCHAR(255),
    date_of_birth DATE,
    gender VARCHAR(20),
    id_type VARCHAR(50),
    id_number VARCHAR(100) UNIQUE,
    preferences TEXT,
    vehicle_registration VARCHAR(50),
    emergency_contact_name VARCHAR(255),
    emergency_contact_phone VARCHAR(50),
    image_url VARCHAR(512),
    voided BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Reservations
CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    guest_id BIGINT,
    room_type_id BIGINT,
    room_id BIGINT,
    date_in DATE,
    date_out DATE,
    adults INT DEFAULT 1,
    children INT DEFAULT 0,
    status VARCHAR(50),
    eta TIME,
    etd TIME,
    special_requests TEXT,
    table_id BIGINT,
    table_reservation_time DATETIME,
    table_pax INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_reservation_guest FOREIGN KEY (guest_id) REFERENCES guests(id)
);

-- Stays
CREATE TABLE IF NOT EXISTS stays (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_id BIGINT,
    guest_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    plan_code VARCHAR(50),
    rate_per_night DECIMAL(19, 2),
    date_in DATETIME NOT NULL,
    date_out DATETIME,
    actual_date_out DATETIME,
    adults INT DEFAULT 1,
    children INT DEFAULT 0,
    is_complimentary BOOLEAN DEFAULT FALSE,
    business_source VARCHAR(50),
    arriving_from VARCHAR(255),
    next_destination VARCHAR(255),
    arrival_flight_no VARCHAR(50),
    departure_flight_no VARCHAR(50),
    card_encoded BOOLEAN DEFAULT FALSE,
    checked_in_by BIGINT,
    checked_out_by BIGINT,
    status VARCHAR(50),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_stay_reservation FOREIGN KEY (reservation_id) REFERENCES reservations(id),
    CONSTRAINT fk_stay_guest FOREIGN KEY (guest_id) REFERENCES guests(id)
);
