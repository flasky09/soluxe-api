package com.hotel_erp.hotel_erp.modules.venue;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class VenueBookingDTO {
    private Long id;
    private Long venueId;
    private String clientName;
    private String clientPhone;
    private String clientNameZh;
    private boolean depositPaid;

    private String clientCompany;
    private EventType eventType;
    private LocalDate dateIn;
    private LocalDate dateOut;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer expectedGuests;
    private SetupType setupType;
    private BigDecimal deposit;
    private BigDecimal totalAmount;
    private BookingStatus status;
    private String notes;
    private Long createdBy;
}
