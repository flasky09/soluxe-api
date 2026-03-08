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
    private String nameZh;
    private String descriptionZh;
    private SpiceLevel spiceLevel;
    private String allergens;
    private boolean isSignature;
    private Integer prepTimeMins;
    private Integer sortOrder;
    private boolean available;

}
