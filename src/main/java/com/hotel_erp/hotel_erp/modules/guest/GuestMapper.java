package com.hotel_erp.hotel_erp.modules.guest;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GuestMapper {
    GuestMapper INSTANCE = Mappers.getMapper(GuestMapper.class);

    @Mapping(target = "idTypeId", source = "idType.id")
    @Mapping(target = "idTypeName", source = "idType.name")
    GuestDTO toDto(GuestEntity entity);

    @Mapping(target = "idType", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    GuestEntity toEntity(GuestDTO dto);
}
