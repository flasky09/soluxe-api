package com.hotel_erp.hotel_erp.modules.inventory.purchaseorder;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseOrderRepository extends BaseRepository<PurchaseOrderEntity, Long> {
    List<PurchaseOrderEntity> findAllByOrderDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT SUM(poi.quantityReceived * poi.unitPrice) FROM PurchaseOrderItemEntity poi JOIN poi.purchaseOrder po WHERE po.status = 'RECEIVED'")
    BigDecimal getTotalAccountsPayable();
}
