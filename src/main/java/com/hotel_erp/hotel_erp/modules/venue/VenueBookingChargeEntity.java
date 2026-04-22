package com.hotel_erp.hotel_erp.modules.venue;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "venue_booking_charges")
@EqualsAndHashCode(callSuper = true)
public class VenueBookingChargeEntity extends BaseEntity {
    
    @Column(name = "venue_booking_id", nullable = false)
    private Long venueBookingId;
    
    private String description;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    
    private boolean voided = false;
    
    @Column(nullable = false)
    private boolean isPaid = false;
}
