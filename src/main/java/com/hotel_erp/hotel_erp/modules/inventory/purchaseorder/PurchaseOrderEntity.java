package com.hotel_erp.hotel_erp.modules.inventory.purchaseorder;

import com.hotel_erp.hotel_erp.modules.inventory.SupplierEntity;
import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "purchase_orders")
@EqualsAndHashCode(callSuper = true)
public class PurchaseOrderEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private SupplierEntity supplier;
    
    private LocalDate orderDate;
    private LocalDate expectedDate;
    
    @Enumerated(EnumType.STRING)
    private POStatus status;
    
    private String notes;
    private Long createdBy;
}
