package com.hotel_erp.hotel_erp.modules.reservation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface ReservationMapper {
    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    @Mapping(target = "nationality", ignore = true)
    @Mapping(target = "idType", ignore = true)
    @Mapping(target = "idNumber", ignore = true)
    @Mapping(target = "preferences", ignore = true)
    @Mapping(target = "vehicleRegistration", ignore = true)
    @Mapping(target = "emergencyContactName", ignore = true)
    @Mapping(target = "emergencyContactPhone", ignore = true)
    ReservationDTO toDto(ReservationEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ReservationEntity toEntity(ReservationDTO dto);
}
