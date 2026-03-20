package com.hotel_erp.hotel_erp.modules.inventory;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface InventoryItemRepository extends BaseRepository<InventoryItemEntity, Long> {
    @Query("SELECT SUM(i.buyingPrice * i.currentStock) FROM InventoryItemEntity i")
    BigDecimal getTotalStockValue();
}
