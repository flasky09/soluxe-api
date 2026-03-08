package com.hotel_erp.hotel_erp.modules.venue;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class VenueBookingChargeDTO {
    private Long id;
    private Long venueBookingId;
    private String description;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private boolean voided;
}
