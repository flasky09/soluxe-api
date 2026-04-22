package com.hotel_erp.hotel_erp.modules.inventory.purchaseorder;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface PurchaseOrderItemMapper {
    @Mapping(source = "purchaseOrder.id", target = "purchaseOrderId")
    @Mapping(source = "inventoryItem.id", target = "inventoryItemId")
    PurchaseOrderItemDTO toDto(PurchaseOrderItemEntity entity);

    @Mapping(source = "purchaseOrderId", target = "purchaseOrder.id")
    @Mapping(source = "inventoryItemId", target = "inventoryItem.id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PurchaseOrderItemEntity toEntity(PurchaseOrderItemDTO dto);
}
