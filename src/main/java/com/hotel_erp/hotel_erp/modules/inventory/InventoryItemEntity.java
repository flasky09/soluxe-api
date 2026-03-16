package com.hotel_erp.hotel_erp.modules.inventory;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "inventory_items")
@EqualsAndHashCode(callSuper = true)
public class InventoryItemEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private InventoryCategoryEntity category;
    
    @Column(unique = true)
    private String name;
    private BigDecimal buyingPrice;
    private BigDecimal currentStock;
    private BigDecimal minimumStock;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private InventoryUnitEntity unit;
    
    private String notes;
}
