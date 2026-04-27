package com.hotel_erp.hotel_erp.modules.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomReportDTO {
    private long totalRooms;
    private long occupiedRooms;
    private long availableRooms;
    private double occupancyRate; // Percentage
    private BigDecimal totalRevenue;
    private BigDecimal adr; // Average Daily Rate
    private BigDecimal revPar; // Revenue Per Available Room
    private long checkIns;
    private long checkOuts;
    private long stayOver;
}
