package com.hotel_erp.hotel_erp.modules.employee.leave;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveRequestDTO {
    private Long id;
    private Long employeeId;
    private LeaveType leaveType;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String reason;
    private LeaveStatus status;
    private Long approvedById;
}
