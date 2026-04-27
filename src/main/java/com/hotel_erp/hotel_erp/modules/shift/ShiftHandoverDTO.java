package com.hotel_erp.hotel_erp.modules.shift;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftHandoverDTO {
    private Long id;
    private Long userId;
    private String username;
    private String fullName;
    private String employeeId;
    private LocalDate date;
    private String shiftType;
    private LocalDateTime clockInTime;
    private LocalDateTime clockOutTime;
    private BigDecimal totalEarnings;
    private long clientsCount;
    private String notes;
    private String status;
}
