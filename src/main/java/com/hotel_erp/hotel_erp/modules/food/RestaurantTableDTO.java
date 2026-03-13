package com.hotel_erp.hotel_erp.modules.food;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RestaurantTableDTO {
    private Long id;
    private String tableName;
    private int capacity;
    private TableLocation location;
    
    @JsonProperty("isVip")
    private boolean isVip;
    private String notes;
    private TableStatus status;
}
