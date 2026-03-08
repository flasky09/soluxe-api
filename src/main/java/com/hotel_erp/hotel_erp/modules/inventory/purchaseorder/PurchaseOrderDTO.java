package com.hotel_erp.hotel_erp.modules.inventory.purchaseorder;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PurchaseOrderDTO {
    private Long id;
    private Long supplierId;
    private LocalDate orderDate;
    private LocalDate expectedDate;
    private POStatus status;
    private String notes;
    private Long createdBy;
}
