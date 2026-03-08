package com.hotel_erp.hotel_erp.modules.inventory;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class InventoryItemDTO {
    private Long id;
    private Long categoryId;
    private Long defaultSupplierId;
    private String name;
    private InventoryUnit unit;
    private BigDecimal currentStock;
    private BigDecimal minimumStock;
    private String notes;
    private String nameZh;
    private BigDecimal unitCost;
}
