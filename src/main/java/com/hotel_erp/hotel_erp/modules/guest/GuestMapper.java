package com.hotel_erp.hotel_erp.modules.guest;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import org.springframework.beans.factory.annotation.Autowired;
import com.hotel_erp.hotel_erp.modules.user.UserRepository;

@Mapper(componentModel = "spring")
public abstract class GuestMapper {

    @Autowired
    protected UserRepository userRepository;

    public abstract GuestDTO toDto(GuestEntity entity);

    @org.mapstruct.AfterMapping
    protected void resolveAuditNames(GuestEntity entity, @org.mapstruct.MappingTarget GuestDTO dto) {
        if (entity.getCreatedBy() != null) {
            userRepository.findById(entity.getCreatedBy())
                    .ifPresent(u -> dto.setCreatedByName(u.getUsername()));
        }
        if (entity.getModifiedBy() != null) {
            userRepository.findById(entity.getModifiedBy())
                    .ifPresent(u -> dto.setModifiedByName(u.getUsername()));
        }
    }

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract GuestEntity toEntity(GuestDTO dto);
}
