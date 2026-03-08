package com.hotel_erp.hotel_erp.modules.venue;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "venue_booking_charges")
@EqualsAndHashCode(callSuper = true)
public class VenueBookingChargeEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_booking_id")
    private VenueBookingEntity venueBooking;
    
    private String description;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private boolean voided;
}

