package com.hotel_erp.hotel_erp.modules.stay;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface StayAdditionalGuestMapper {
    StayAdditionalGuestMapper INSTANCE = Mappers.getMapper(StayAdditionalGuestMapper.class);

    StayAdditionalGuestDTO toDto(StayAdditionalGuestEntity entity);

    @Mapping(target = "idType", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    StayAdditionalGuestEntity toEntity(StayAdditionalGuestDTO dto);
}
