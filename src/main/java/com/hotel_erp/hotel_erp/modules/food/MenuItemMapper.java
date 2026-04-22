package com.hotel_erp.hotel_erp.modules.food;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface MenuItemMapper {
    MenuItemMapper INSTANCE = Mappers.getMapper(MenuItemMapper.class);

    @Mapping(target = "categoryId", source = "category.id")
    MenuItemDTO toDto(MenuItemEntity entity);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "nameZh", ignore = true)
    @Mapping(target = "descriptionZh", ignore = true)
    @Mapping(target = "spiceLevel", ignore = true)
    @Mapping(target = "allergens", ignore = true)
    @Mapping(target = "signature", ignore = true)
    @Mapping(target = "sortOrder", ignore = true)
    MenuItemEntity toEntity(MenuItemDTO dto);
}
