CREATE TABLE IF NOT EXISTS room_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    default_rate DECIMAL(19, 2),
    weekend_rate DECIMAL(19, 2),
    capacity INTEGER,
    bed_type VARCHAR(255),
    amenities TEXT,
    created_at DATETIME(6),
    updated_at DATETIME(6)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS rooms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(255),
    floor VARCHAR(255),
    room_type_id BIGINT NOT NULL,
    status VARCHAR(255),
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT fk_room_type FOREIGN KEY (room_type_id) REFERENCES room_types(id)
) ENGINE=InnoDB;
