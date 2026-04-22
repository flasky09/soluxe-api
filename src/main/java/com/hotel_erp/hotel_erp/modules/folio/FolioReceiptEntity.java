package com.hotel_erp.hotel_erp.modules.folio;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "folio_receipts")
@EqualsAndHashCode(callSuper = true)
public class FolioReceiptEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String receiptNumber;

    @Column(nullable = false)
    private Long folioId;

    @Column(nullable = false)
    private Long paymentId;

    @Column(nullable = false)
    private BigDecimal amount;

    private LocalDateTime issuedAt;
    private Long issuedBy;
}
