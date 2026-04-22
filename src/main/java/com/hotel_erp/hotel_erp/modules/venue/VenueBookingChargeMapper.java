package com.hotel_erp.hotel_erp.modules.venue;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface VenueBookingChargeMapper {

    VenueBookingChargeDTO toDto(VenueBookingChargeEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    VenueBookingChargeEntity toEntity(VenueBookingChargeDTO dto);
}
