package com.hotel_erp.hotel_erp.modules.maintenance;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MaintenanceMapper {
    @Mapping(target = "issueTypeId", source = "issueType.id")
    @Mapping(target = "issueTypeName", source = "issueType.name")
    MaintenanceDTO toDto(MaintenanceEntity entity);

    @Mapping(target = "issueType", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    MaintenanceEntity toEntity(MaintenanceDTO dto);
}
