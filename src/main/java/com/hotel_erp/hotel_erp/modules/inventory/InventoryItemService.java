package com.hotel_erp.hotel_erp.modules.inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryItemService {
    List<InventoryItemDTO> findAllItems();

    Optional<InventoryItemDTO> findItemById(Long id);

    InventoryItemDTO saveItem(InventoryItemDTO dto);

    void deleteById(Long id);
}

