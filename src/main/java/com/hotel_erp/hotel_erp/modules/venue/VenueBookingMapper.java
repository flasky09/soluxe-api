package com.hotel_erp.hotel_erp.modules.venue;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VenueBookingMapper {
    @Mapping(source = "venue.id", target = "venueId")
    @Mapping(source = "venue.name", target = "venueName")
    VenueBookingDTO toDto(VenueBookingEntity entity);

    @Mapping(source = "venueId", target = "venue.id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    VenueBookingEntity toEntity(VenueBookingDTO dto);
}
