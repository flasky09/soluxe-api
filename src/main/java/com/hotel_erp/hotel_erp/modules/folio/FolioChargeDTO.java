package com.hotel_erp.hotel_erp.modules.folio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolioChargeDTO {
    private Long id;
    private Long folioId;
    private ChargeType chargeType;
    private String description;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountPct;
    private BigDecimal taxPct;
    private BigDecimal totalAmount;
    private LocalDateTime chargedAt;
    private Long addedBy;
    private String referenceId;
    private boolean voided;
    private String voidReason;
    private Long voidedBy;
}
