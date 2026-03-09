package com.hotel_erp.hotel_erp.modules.food;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MenuItemDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private boolean available;
    private Integer prepTimeMins;
}
