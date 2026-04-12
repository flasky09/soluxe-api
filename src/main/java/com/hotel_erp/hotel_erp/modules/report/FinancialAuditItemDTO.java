package com.hotel_erp.hotel_erp.modules.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialAuditItemDTO {
    private LocalDateTime timestamp;
    private String type; // e.g., "PAYMENT", "CHARGE", "EXPENSE", "PURCHASE"
    private String reference; // Folio #, Receipt #, Supplier Name
    private String description;
    private BigDecimal amount;
    private BigDecimal runningBalance;
    private String account;
    private String status;
}
