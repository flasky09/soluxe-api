package com.hotel_erp.hotel_erp.modules.inventory;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class InventoryItemDTO {
    private Long id;
    private Long categoryId;
    private String name;
    private Long unitId;
    private String unitName;
    private BigDecimal currentStock;
    private BigDecimal minimumStock;
    private String notes;
    private BigDecimal unitCost;
    private BigDecimal buyingPrice;
}
