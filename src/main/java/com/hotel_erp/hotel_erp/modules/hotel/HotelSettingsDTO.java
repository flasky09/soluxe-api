package com.hotel_erp.hotel_erp.modules.hotel;

import lombok.Data;

@Data
public class HotelSettingsDTO {
    private Long id;
    private boolean allowWalkIn;
    private boolean allowComplimentary;
}
