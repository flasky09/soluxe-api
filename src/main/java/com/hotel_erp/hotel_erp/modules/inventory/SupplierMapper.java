package com.hotel_erp.hotel_erp.modules.inventory;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface SupplierMapper {
    SupplierDTO toDto(SupplierEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SupplierEntity toEntity(SupplierDTO dto);
}
