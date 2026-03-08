package com.hotel_erp.hotel_erp.modules.inventory.purchaseorder;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PurchaseOrderItemDTO {
    private Long id;
    private Long purchaseOrderId;
    private Long inventoryItemId;
    private BigDecimal quantityOrdered;
    private BigDecimal quantityReceived;
    private BigDecimal unitPrice;
}
