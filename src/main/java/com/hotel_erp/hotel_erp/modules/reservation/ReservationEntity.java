package com.hotel_erp.hotel_erp.modules.reservation;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "reservations")
@EqualsAndHashCode(callSuper = true)
public class ReservationEntity extends BaseEntity {
    private Long guestId;
    private Long roomTypeId;
    private Long roomId;
    private LocalDate dateIn;
    private LocalDate dateOut;
    private int adults;
    private int children;
    
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private java.time.LocalTime eta;
    private java.time.LocalTime etd;
    private String specialRequests;
}
