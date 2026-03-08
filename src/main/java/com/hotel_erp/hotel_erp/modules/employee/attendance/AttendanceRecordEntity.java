package com.hotel_erp.hotel_erp.modules.employee.attendance;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import com.hotel_erp.hotel_erp.modules.employee.EmployeeEntity;
import com.hotel_erp.hotel_erp.modules.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance_records")
@Getter
@Setter
public class AttendanceRecordEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "clock_in")
    private LocalTime clockIn;

    @Column(name = "clock_out")
    private LocalTime clockOut;

    @Column(name = "hours_worked")
    private BigDecimal hoursWorked;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AttendanceStatus status;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by")
    private UserEntity recordedBy;
}
