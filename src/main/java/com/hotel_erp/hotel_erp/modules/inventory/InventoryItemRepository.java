package com.hotel_erp.hotel_erp.modules.inventory;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends BaseRepository<InventoryItemEntity, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT SUM(i.buyingPrice * i.currentStock) FROM InventoryItemEntity i")
    java.math.BigDecimal getTotalStockValue();
}
