package com.hotel_erp.hotel_erp.modules.food;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DiningSessionMapper {
    @Mapping(source = "table.id", target = "tableId")
    @Mapping(source = "stay.id", target = "stayId")
    @Mapping(source = "servedBy.id", target = "servedById")
    DiningSessionDTO toDto(DiningSessionEntity entity);

    @Mapping(source = "tableId", target = "table.id")
    @Mapping(source = "stayId", target = "stay.id")
    @Mapping(source = "servedById", target = "servedBy.id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    DiningSessionEntity toEntity(DiningSessionDTO dto);
}
