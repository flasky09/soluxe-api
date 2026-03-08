package com.hotel_erp.hotel_erp.modules.venue;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class VenueDTO {
    private Long id;
    private String name;
    private VenueType type;
    private Integer capacity;
    private BigDecimal ratePerHour;
    private BigDecimal ratePerDay;
    private String description;
}
