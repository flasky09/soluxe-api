package com.hotel_erp.hotel_erp.modules.folio;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Builder;

import org.springframework.beans.factory.annotation.Autowired;
import com.hotel_erp.hotel_erp.modules.user.UserRepository;

@Mapper(componentModel = "spring")
public abstract class FolioMapper {

    @Autowired
    protected UserRepository userRepository;

    public abstract FolioDTO toDto(FolioEntity entity);

    @org.mapstruct.AfterMapping
    protected void resolveAuditNames(FolioEntity entity, @org.mapstruct.MappingTarget FolioDTO dto) {
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
    @Mapping(target = "version", ignore = true)
    public abstract FolioEntity toEntity(FolioDTO dto);
}
