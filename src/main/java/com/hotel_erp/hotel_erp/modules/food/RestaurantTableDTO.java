package com.hotel_erp.hotel_erp.modules.food;

import lombok.Data;

@Data
public class RestaurantTableDTO {
    private Long id;
    private String tableName;
    private int capacity;

    private TableLocation location;
    private boolean isVip;
    private String notes;
}
