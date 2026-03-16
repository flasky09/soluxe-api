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
    
    // Financials
    private double dailyRevenue;
    private double averageDailyRate;
    private double revenuePerAvailableRoom;
    
    // Alerts/Actions
    private long pendingHousekeeping;
    private long pendingLeaveRequests;
    private long pendingPurchaseOrders;
    private long lowStockItems;
}
