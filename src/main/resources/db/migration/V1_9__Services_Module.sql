-- Services Module: Venues, Housekeeping, and Maintenance

-- Venues
CREATE TABLE IF NOT EXISTS venues (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(100),
    capacity INT,
    rate_per_hour DECIMAL(19, 2),
    rate_per_day DECIMAL(19, 2),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Venue Bookings
CREATE TABLE IF NOT EXISTS venue_bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    venue_id BIGINT,
    client_name VARCHAR(255),
    client_name_zh VARCHAR(255),
    client_phone VARCHAR(50),
    client_company VARCHAR(255),
    event_type VARCHAR(50),
    date_in DATE,
    date_out DATE,
    start_time TIME,
    end_time TIME,
    expected_guests INT,
    setup_type VARCHAR(50),
    deposit DECIMAL(19, 2),
    deposit_paid BOOLEAN DEFAULT FALSE,
    total_amount DECIMAL(19, 2),
    status VARCHAR(50),
    notes TEXT,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_booking_venue FOREIGN KEY (venue_id) REFERENCES venues(id)
);

-- Venue Booking Charges
CREATE TABLE IF NOT EXISTS venue_booking_charges (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    venue_booking_id BIGINT,
    charge_type_id BIGINT,
    description TEXT,
    amount DECIMAL(19, 2),
    charged_at DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_venue_charge_booking FOREIGN KEY (venue_booking_id) REFERENCES venue_bookings(id),
    CONSTRAINT fk_venue_charge_type FOREIGN KEY (charge_type_id) REFERENCES charge_types(id)
);

-- Housekeeping Logs
CREATE TABLE IF NOT EXISTS housekeeping_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT NOT NULL,
    action VARCHAR(50) NOT NULL,
    notes TEXT,
    performed_by BIGINT NOT NULL,
    performed_at DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Maintenance Issue Types
CREATE TABLE IF NOT EXISTS maintenance_issue_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Maintenance Tickets
CREATE TABLE IF NOT EXISTS maintenance_tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT,
    reported_by BIGINT NOT NULL,
    assigned_to BIGINT,
    issue_type_id BIGINT,
    description TEXT NOT NULL,
    status VARCHAR(50) DEFAULT 'OPEN',
    priority VARCHAR(50) DEFAULT 'MEDIUM',
    resolved_at DATETIME,
    resolution_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_maintenance_issue_type FOREIGN KEY (issue_type_id) REFERENCES maintenance_issue_types(id)
);
