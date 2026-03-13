package com.hotel_erp.hotel_erp.modules.inventory.purchaseorder;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {
    @Mapping(source = "supplier.id", target = "supplierId")
    PurchaseOrderDTO toDto(PurchaseOrderEntity entity);

    @Mapping(source = "supplierId", target = "supplier.id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PurchaseOrderEntity toEntity(PurchaseOrderDTO dto);
}
