package com.hotel_erp.hotel_erp.modules.reservation;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
@EqualsAndHashCode(callSuper = true)
public class ReservationEntity extends BaseEntity {
    private Long guestId;
    private Long roomTypeId;
    private Long roomId;
    
    // Room stay dates
    private LocalDate dateIn;
    private LocalDate dateOut;
    
    private int adults;
    private int children;
    
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private LocalTime eta;
    private LocalTime etd;
    private String specialRequests;

    // Table reservation fields
    private Long tableId;
    private LocalDateTime tableReservationTime;
    private Integer tablePax;
}
