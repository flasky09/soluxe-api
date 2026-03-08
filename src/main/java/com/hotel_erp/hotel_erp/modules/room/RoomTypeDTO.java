package com.hotel_erp.hotel_erp.modules.room;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomTypeDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal defaultRate;
}
