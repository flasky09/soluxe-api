package com.hotel_erp.hotel_erp.modules.employee.attendance;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AttendanceRecordDTO {
    private Long id;
    private Long employeeId;
    private LocalDate date;
    private LocalTime clockIn;
    private LocalTime clockOut;
    private BigDecimal hoursWorked;
    private AttendanceStatus status;
    private String notes;
    private Long recordedById;
}
