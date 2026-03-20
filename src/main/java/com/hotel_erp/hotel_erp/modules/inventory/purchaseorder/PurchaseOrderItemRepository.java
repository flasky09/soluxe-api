package com.hotel_erp.hotel_erp.modules.inventory.purchaseorder;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface PurchaseOrderItemRepository extends BaseRepository<PurchaseOrderItemEntity, Long> {
    @Query("SELECT SUM(poi.unitPrice * poi.quantityOrdered) FROM PurchaseOrderItemEntity poi WHERE poi.purchaseOrder.orderDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalSpendInDateRange(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate);
}
