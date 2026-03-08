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
public class FolioReceiptDTO {
    private Long id;
    private String receiptNumber;
    private Long paymentId;
    private Long folioId;
    private BigDecimal amount;
    private LocalDateTime issuedAt;
    private Long issuedBy;
}
