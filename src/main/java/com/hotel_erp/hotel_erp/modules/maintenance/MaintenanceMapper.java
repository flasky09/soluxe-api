package com.hotel_erp.hotel_erp.modules.maintenance;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface MaintenanceMapper {

    MaintenanceDTO toDto(MaintenanceEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    MaintenanceEntity toEntity(MaintenanceDTO dto);
}
