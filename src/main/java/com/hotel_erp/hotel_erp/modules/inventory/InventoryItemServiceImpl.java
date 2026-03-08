package com.hotel_erp.hotel_erp.modules.inventory;

import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class InventoryItemServiceImpl extends BaseServiceImpl<InventoryItemEntity, Long, InventoryItemRepository> implements InventoryItemService {
    public InventoryItemServiceImpl(InventoryItemRepository repository) {
        super(repository);
    }
}

