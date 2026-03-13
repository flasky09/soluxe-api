package com.hotel_erp.hotel_erp.modules.inventory;

import lombok.Data;

@Data
public class InventoryUnitDTO {
    private Long id;
    private String name;
    private String description;
    private boolean active;
}
