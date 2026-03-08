package com.hotel_erp.hotel_erp.modules.folio;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "folio_receipts")
public class FolioReceiptEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String receiptNumber;

    @Column(nullable = false)
    private Long paymentId;

    @Column(nullable = false)
    private Long folioId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Column(nullable = false)
    private Long issuedBy;
}
