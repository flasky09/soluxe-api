package com.hotel_erp.hotel_erp.modules.report;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
public class ProfitAndLossDTO {
    private LocalDate startDate;
    private LocalDate endDate;

    // Revenue
    private BigDecimal totalRevenue;
    private Map<String, BigDecimal> revenueByDepartment;

    // Cost of Goods Sold (COGS) - Supplies/Stock used
    private BigDecimal costOfSales; 
    private BigDecimal grossProfit;

    // Operating Expenses (OpEx)
    private BigDecimal payrollExpenses;
    private BigDecimal operationalExpenses; // Utilities, Maintenance, etc.
    private BigDecimal maintenanceCosts;
    private BigDecimal pettyCashExpenses;
    
    private BigDecimal totalOperatingExpenses;
    
    // Net Result
    private BigDecimal netProfit;
    private BigDecimal operatingMargin; // Percentage
}
