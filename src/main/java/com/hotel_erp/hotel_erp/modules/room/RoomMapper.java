package com.hotel_erp.hotel_erp.modules.room;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {RoomTypeMapper.class})
public interface RoomMapper {
    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    @Mapping(target = "roomTypeId", source = "roomType.id")
    @Mapping(target = "roomType", source = "roomType")
    RoomDTO toDto(RoomEntity entity);

    @Mapping(target = "roomType.id", source = "roomTypeId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RoomEntity toEntity(RoomDTO dto);
}
