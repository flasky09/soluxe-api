package com.hotel_erp.hotel_erp.modules.inventory.purchaseorder;

import com.hotel_erp.hotel_erp.modules.inventory.InventoryItemEntity;
import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "purchase_order_items")
@EqualsAndHashCode(callSuper = true)
public class PurchaseOrderItemEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrderEntity purchaseOrder;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id")
    private InventoryItemEntity inventoryItem;
    
    private BigDecimal quantityOrdered;
    private BigDecimal quantityReceived;
    private BigDecimal unitPrice;
}
