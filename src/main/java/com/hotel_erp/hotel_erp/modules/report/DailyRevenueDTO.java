package com.hotel_erp.hotel_erp.modules.report;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private Map<String, BigDecimal> revenueByChargeType;
}
