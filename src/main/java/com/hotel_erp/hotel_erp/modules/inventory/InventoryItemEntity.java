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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private SupplierEntity defaultSupplier;
    
    private String name;
    private String nameZh;
    private BigDecimal unitCost;

    
    @Enumerated(EnumType.STRING)
    private InventoryUnit unit;
    
    private BigDecimal currentStock;
    private BigDecimal minimumStock;
    private String notes;
}
