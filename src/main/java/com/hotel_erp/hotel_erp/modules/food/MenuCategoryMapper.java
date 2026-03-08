package com.hotel_erp.hotel_erp.modules.food;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MenuCategoryMapper {
    MenuCategoryMapper INSTANCE = Mappers.getMapper(MenuCategoryMapper.class);

    MenuCategoryDTO toDto(MenuCategoryEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "description", ignore = true)
    MenuCategoryEntity toEntity(MenuCategoryDTO dto);
}
