package com.hotel_erp.hotel_erp.modules.food;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RestaurantTableMapper {
    RestaurantTableMapper INSTANCE = Mappers.getMapper(RestaurantTableMapper.class);

    RestaurantTableDTO toDto(RestaurantTableEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RestaurantTableEntity toEntity(RestaurantTableDTO dto);
}
