package com.hotel_erp.hotel_erp.modules.reservation;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReservationDTO {
    private Long id;
    private Long guestId;
    private Long roomTypeId;
    private Long roomId;
    private LocalDate dateIn;
    private LocalDate dateOut;
    private int adults;
    private int children;
    private String status;
    
    private String nationality;
    private String idType;
    private String idNumber;
    
    // Stay specific
    private String eta;
    private String etd;
    private String specialRequests;

    // Guest profile sync
    private String preferences;
    private String vehicleRegistration;
    private String emergencyContactName;
    private String emergencyContactPhone;

    // Table reservation fields
    private Long tableId;
    private LocalDateTime tableReservationTime;
    private Integer tablePax;
}
