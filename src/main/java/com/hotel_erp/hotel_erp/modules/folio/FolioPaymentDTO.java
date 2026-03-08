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
public class FolioPaymentDTO {
    private Long id;
    private Long folioId;
    private Long paymentMethodId;
    private String paymentMethodName; // Optional for UI display
    private BigDecimal amount;
    private String referenceNumber;
    private Long recordedBy;
    private LocalDateTime recordedAt;
}
