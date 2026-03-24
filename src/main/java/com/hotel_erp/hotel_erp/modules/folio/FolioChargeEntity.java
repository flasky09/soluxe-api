package com.hotel_erp.hotel_erp.modules.folio;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "folio_charges")
@EqualsAndHashCode(callSuper = true)
public class FolioChargeEntity extends BaseEntity {

    @Column(nullable = false)
    private Long folioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_type_id")
    private ChargeTypeEntity chargeType;

    private String description;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountPct = BigDecimal.ZERO;
    private BigDecimal taxPct = BigDecimal.ZERO;
    private BigDecimal totalAmount;

    private LocalDateTime chargedAt;
    private Long addedBy;

    private String referenceId;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private boolean voided;
    private String voidReason;
    private Long voidedBy;
}

