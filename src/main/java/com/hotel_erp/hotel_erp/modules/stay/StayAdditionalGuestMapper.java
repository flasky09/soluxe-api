package com.hotel_erp.hotel_erp.modules.stay;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StayAdditionalGuestMapper {
    StayAdditionalGuestMapper INSTANCE = Mappers.getMapper(StayAdditionalGuestMapper.class);

    @Mapping(target = "idTypeId", source = "idType.id")
    @Mapping(target = "idTypeName", source = "idType.name")
    StayAdditionalGuestDTO toDto(StayAdditionalGuestEntity entity);

    @Mapping(target = "idType", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    StayAdditionalGuestEntity toEntity(StayAdditionalGuestDTO dto);
}
