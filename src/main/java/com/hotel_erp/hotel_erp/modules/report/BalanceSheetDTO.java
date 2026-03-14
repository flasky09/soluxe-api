package com.hotel_erp.hotel_erp.modules.report;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class BalanceSheetDTO {
    private LocalDate asOfDate;

    // Current Assets
    private BigDecimal cashOnHand; // Liquidity
    private BigDecimal accountsReceivable;
    private BigDecimal inventoryValue;
    private BigDecimal totalCurrentAssets;

    // Fixed Assets
    private BigDecimal fixedAssetsValue; // Cumulative asset buys
    private BigDecimal totalAssets;

    // Liabilities
    private BigDecimal accountsPayable;
    private BigDecimal taxPayable; // Collected but not remitted (future feature)
    private BigDecimal totalLiabilities;

    // Equity
    private BigDecimal capitalInjected;
    private BigDecimal retainedEarnings; // Cumulative Profit - Drawings
    private BigDecimal totalEquity;
}
