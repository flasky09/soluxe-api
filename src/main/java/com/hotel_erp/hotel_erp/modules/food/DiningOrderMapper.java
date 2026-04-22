package com.hotel_erp.hotel_erp.modules.food;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface DiningOrderMapper {
    @Mapping(source = "session.id", target = "sessionId")
    @Mapping(source = "menuItem.id", target = "menuItemId")
    DiningOrderDTO toDto(DiningOrderEntity entity);

    @Mapping(source = "sessionId", target = "session.id")
    @Mapping(source = "menuItemId", target = "menuItem.id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    DiningOrderEntity toEntity(DiningOrderDTO dto);
}
