package com.hotel_erp.hotel_erp.modules.venue;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface VenueBookingMapper {

    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    VenueBookingDTO toDto(VenueBookingEntity entity);

    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    VenueBookingEntity toEntity(VenueBookingDTO dto);

    default LocalTime toLocalTime(LocalDateTime dt) {
        return dt != null ? dt.toLocalTime() : null;
    }

    default LocalDateTime toLocalDateTime(LocalTime time) {
        return time != null ? LocalDateTime.of(LocalDate.now(), time) : null;
    }
}
