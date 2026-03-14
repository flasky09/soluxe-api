package com.hotel_erp.hotel_erp.modules.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyRevenueDTO {
    private LocalDate reportDate;
    private BigDecimal totalRevenue;
    private BigDecimal netRevenue;
    private BigDecimal taxCollected;
    private BigDecimal totalPayments;
    private BigDecimal totalExpenses;
    private BigDecimal operationalExpenses;
    private BigDecimal totalAssets;
    private BigDecimal maintenanceCosts;
    private BigDecimal supplyCosts;
    private BigDecimal payrollExpenses;
    private BigDecimal inventoryValue;
    private BigDecimal totalDrawings;
    private BigDecimal totalSavings;
    private BigDecimal totalCapitalInjected;
    private BigDecimal accountsReceivable;
    private BigDecimal accountsPayable;
    private BigDecimal pettyCash;
    private Map<String, BigDecimal> revenueByChargeType;
    private List<FinancialAuditItemDTO> auditTray;
}
