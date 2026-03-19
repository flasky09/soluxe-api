package com.hotel_erp.hotel_erp.modules.folio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolioChargeDTO {
    private Long id;
    private Long folioId;
    private Long chargeTypeId;
    private String chargeTypeName;
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.01", message = "Quantity must be greater than zero")
    private BigDecimal quantity;
    @NotNull(message = "Unit price is required")
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
