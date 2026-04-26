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
public class UserPerformanceDTO {
    private Long userId;
    private String username;
    private String fullName;
    private long checkIns;
    private long checkOuts;
    private BigDecimal totalCollected;
}
