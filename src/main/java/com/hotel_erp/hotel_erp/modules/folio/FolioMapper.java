package com.hotel_erp.hotel_erp.modules.folio;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FolioMapper {
    FolioMapper INSTANCE = Mappers.getMapper(FolioMapper.class);

    FolioDTO toDto(FolioEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FolioEntity toEntity(FolioDTO dto);
}
