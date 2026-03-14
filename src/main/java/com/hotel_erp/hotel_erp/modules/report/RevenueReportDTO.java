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
public class RevenueReportDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalRevenue;
    private BigDecimal netRevenue;
    private BigDecimal taxCollected;
    private BigDecimal totalPayments;
    private BigDecimal totalExpenses; // Total spent (OpEx + CapEx)
    private BigDecimal operationalExpenses; // Only non-asset expenses
    private BigDecimal totalAssets; // Value of fixed assets bought
    private BigDecimal maintenanceCosts; // Sub-category of operational expenses
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
