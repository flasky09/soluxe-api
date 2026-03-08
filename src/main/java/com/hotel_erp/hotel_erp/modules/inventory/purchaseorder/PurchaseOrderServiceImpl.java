package com.hotel_erp.hotel_erp.modules.inventory.purchaseorder;

import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderServiceImpl extends BaseServiceImpl<PurchaseOrderEntity, Long, PurchaseOrderRepository> implements PurchaseOrderService {
    public PurchaseOrderServiceImpl(PurchaseOrderRepository repository) {
        super(repository);
    }
}

