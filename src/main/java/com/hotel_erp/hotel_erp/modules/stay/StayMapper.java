package com.hotel_erp.hotel_erp.modules.stay;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface StayMapper {
    StayMapper INSTANCE = Mappers.getMapper(StayMapper.class);

    StayDTO toDto(StayEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "arrivingFrom", ignore = true)
    @Mapping(target = "nextDestination", ignore = true)
    @Mapping(target = "arrivalFlightNo", ignore = true)
    @Mapping(target = "departureFlightNo", ignore = true)
    @Mapping(target = "cardEncoded", ignore = true)
    @Mapping(target = "checkedInBy", ignore = true)
    @Mapping(target = "checkedOutBy", ignore = true)
    @Mapping(target = "notes", ignore = true)
    StayEntity toEntity(StayDTO dto);
}
