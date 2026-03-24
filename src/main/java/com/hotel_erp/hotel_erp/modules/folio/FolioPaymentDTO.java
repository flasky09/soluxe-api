package com.hotel_erp.hotel_erp.modules.folio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolioPaymentDTO {
    private Long id;
    private Long folioId;
    @NotNull(message = "Payment method is required")
    private Long paymentMethodId;
    private String paymentMethodName; 
    @NotNull(message = "Amount is required")
    private BigDecimal amount;
    private String referenceNumber;
    private String notes;
    private Long recordedBy;
    private LocalDateTime recordedAt;
}
