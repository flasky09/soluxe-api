package com.hotel_erp.hotel_erp.modules.inventory;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryItemMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(target = "unitId", source = "unit.id")
    @Mapping(target = "unitName", source = "unit.name")
    InventoryItemDTO toDto(InventoryItemEntity entity);

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    InventoryItemEntity toEntity(InventoryItemDTO dto);
}
