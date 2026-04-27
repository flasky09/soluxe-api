package com.hotel_erp.hotel_erp.modules.shift;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shift_handovers")
@EqualsAndHashCode(callSuper = true)
public class ShiftHandoverEntity extends BaseEntity {

    @Column(nullable = false)
    private Long userId;

    private String employeeId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String shiftType; // e.g., "DAY_SHIFT", "NIGHT_SHIFT"

    @Column(nullable = false)
    private LocalDateTime clockInTime;

    private LocalDateTime clockOutTime;

    private BigDecimal totalEarnings;

    private long clientsCount;

    @Column(length = 1000)
    private String notes;

    @Enumerated(EnumType.STRING)
    private ShiftStatus status;

    public enum ShiftStatus {
        ACTIVE,
        CLOSED
    }
}
