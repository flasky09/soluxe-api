package com.hotel_erp.hotel_erp.modules.hotel;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface HotelSettingsMapper {
    HotelSettingsMapper INSTANCE = Mappers.getMapper(HotelSettingsMapper.class);

    HotelSettingsDTO toDto(HotelSettingsEntity entity);

    @org.mapstruct.Mapping(target = "createdAt", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAt", ignore = true)
    HotelSettingsEntity toEntity(HotelSettingsDTO dto);
}
