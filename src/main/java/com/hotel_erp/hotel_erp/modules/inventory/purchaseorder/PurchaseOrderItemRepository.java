package com.hotel_erp.hotel_erp.modules.inventory.purchaseorder;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderItemRepository extends com.hotel_erp.hotel_erp.shared.BaseRepository<PurchaseOrderItemEntity, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT SUM(poi.unitPrice * poi.quantityOrdered) FROM PurchaseOrderItemEntity poi WHERE poi.purchaseOrder.orderDate BETWEEN :startDate AND :endDate")
    java.math.BigDecimal getTotalSpendInDateRange(
        @org.springframework.data.repository.query.Param("startDate") java.time.LocalDate startDate, 
        @org.springframework.data.repository.query.Param("endDate") java.time.LocalDate endDate);
}
