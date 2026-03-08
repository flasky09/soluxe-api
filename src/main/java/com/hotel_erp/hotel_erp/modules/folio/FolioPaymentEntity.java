package com.hotel_erp.hotel_erp.modules.folio;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "folio_payments")
public class FolioPaymentEntity extends BaseEntity {
    private Long folioId;
    private Long paymentMethodId;
    private BigDecimal amount;
    private String referenceNumber;
    private Long recordedBy;
    private LocalDateTime recordedAt;
}
