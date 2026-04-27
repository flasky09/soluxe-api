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
@Table(name = "folio_payments")
@EqualsAndHashCode(callSuper = true)
public class FolioPaymentEntity extends BaseEntity {

    @Column(nullable = false)
    private Long folioId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(length = 10)
    private String currencyCode;

    @Column(precision = 19, scale = 4)
    private BigDecimal exchangeRate;

    private String referenceNumber;
    private LocalDateTime recordedAt;
    private Long recordedBy;
    
    private String notes;
    private boolean voided;
    private String voidReason;
}
