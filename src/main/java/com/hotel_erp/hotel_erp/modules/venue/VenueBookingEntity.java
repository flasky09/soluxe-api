package com.hotel_erp.hotel_erp.modules.venue;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "venue_bookings")
@EqualsAndHashCode(callSuper = true)
public class VenueBookingEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    private VenueEntity venue;
    
    private String clientName;
    private String clientNameZh;
    private String clientPhone;

    private String clientCompany;
    
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    
    private LocalDate dateIn;
    private LocalDate dateOut;
    private LocalTime startTime;
    private LocalTime endTime;
    
    private Integer expectedGuests;
    
    @Enumerated(EnumType.STRING)
    private SetupType setupType;
    
    private BigDecimal deposit;
    private boolean depositPaid;
    private BigDecimal totalAmount;

    
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    
    private String notes;
    private Long createdBy;
}
