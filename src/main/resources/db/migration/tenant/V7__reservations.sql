-- Module: Reservations

CREATE TABLE IF NOT EXISTS reservations (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    guest_id                BIGINT,
    room_type_id            BIGINT,
    room_id                 BIGINT,
    date_in                 DATE,
    date_out                DATE,
    adults                  INT NOT NULL DEFAULT 1,
    children                INT NOT NULL DEFAULT 0,
    status                  VARCHAR(50),
    eta                     TIME,
    etd                     TIME,
    special_requests        TEXT,
    table_id                BIGINT,
    table_reservation_time  DATETIME(6),
    table_pax               INT,
    created_at              DATETIME(6),
    updated_at              DATETIME(6)
);
