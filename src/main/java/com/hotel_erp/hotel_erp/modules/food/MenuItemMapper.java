package com.hotel_erp.hotel_erp.modules.food;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {
    MenuItemMapper INSTANCE = Mappers.getMapper(MenuItemMapper.class);

    @Mapping(target = "categoryId", source = "category.id")
    MenuItemDTO toDto(MenuItemEntity entity);

    @Mapping(target = "category.id", source = "categoryId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    MenuItemEntity toEntity(MenuItemDTO dto);
}
