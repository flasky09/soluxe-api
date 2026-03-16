-- Create folio_receipts table
CREATE TABLE IF NOT EXISTS folio_receipts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receipt_number VARCHAR(255) NOT NULL UNIQUE,
    payment_id BIGINT NOT NULL,
    folio_id BIGINT NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    issued_at DATETIME NOT NULL,
    issued_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_receipt_payment FOREIGN KEY (payment_id) REFERENCES folio_payments(id),
    CONSTRAINT fk_receipt_folio FOREIGN KEY (folio_id) REFERENCES folios(id)
);
