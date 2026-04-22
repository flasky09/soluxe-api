package com.hotel_erp.hotel_erp.modules.folio;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Builder;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface FolioChargeMapper {
    FolioChargeMapper INSTANCE = Mappers.getMapper(FolioChargeMapper.class);

    FolioChargeDTO toDto(FolioChargeEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FolioChargeEntity toEntity(FolioChargeDTO dto);
}
