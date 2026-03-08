package com.hotel_erp.hotel_erp.modules.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {
    private long totalArrivalsToday;
    private long totalDeparturesToday;
    private long activeStays;
    private double occupancyRate;
}
