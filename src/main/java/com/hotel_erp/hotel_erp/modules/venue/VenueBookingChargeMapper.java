package com.hotel_erp.hotel_erp.modules.venue;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VenueBookingChargeMapper {
    @Mapping(source = "venueBooking.id", target = "venueBookingId")
    VenueBookingChargeDTO toDto(VenueBookingChargeEntity entity);

    @Mapping(source = "venueBookingId", target = "venueBooking.id")
    @Mapping(target = "voided", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    VenueBookingChargeEntity toEntity(VenueBookingChargeDTO dto);
}
