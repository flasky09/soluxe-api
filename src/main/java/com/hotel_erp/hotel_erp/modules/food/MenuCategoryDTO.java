package com.hotel_erp.hotel_erp.modules.food;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MenuCategoryDTO {
    private Long id;
    private String name;
    private String nameZh;
    private Integer sortOrder;
    
    @JsonProperty("isActive")
    private boolean isActive;
}
