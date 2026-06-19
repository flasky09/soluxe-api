package com.hotel_erp.hotel_erp.modules.venue;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import com.hotel_erp.hotel_erp.modules.user.UserRepository;

@Mapper(componentModel = "spring")
public abstract class VenueBookingMapper {

    @Autowired
    protected UserRepository userRepository;

    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    public abstract VenueBookingDTO toDto(VenueBookingEntity entity);

    @org.mapstruct.AfterMapping
    protected void resolveAuditNames(VenueBookingEntity entity, @org.mapstruct.MappingTarget VenueBookingDTO dto) {
        if (entity.getCreatedBy() != null) {
            userRepository.findById(entity.getCreatedBy())
                    .ifPresent(u -> dto.setCreatedByName(u.getUsername()));
        }
        if (entity.getModifiedBy() != null) {
            userRepository.findById(entity.getModifiedBy())
                    .ifPresent(u -> dto.setModifiedByName(u.getUsername()));
        }
    }

    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract VenueBookingEntity toEntity(VenueBookingDTO dto);

    protected LocalTime toLocalTime(LocalDateTime dt) {
        return dt != null ? dt.toLocalTime() : null;
    }

    protected LocalDateTime toLocalDateTime(LocalTime time) {
        return time != null ? LocalDateTime.of(LocalDate.now(), time) : null;
    }
}
