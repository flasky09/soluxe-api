package com.hotel_erp.hotel_erp.modules.folio;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FolioChargeMapper {
    FolioChargeMapper INSTANCE = Mappers.getMapper(FolioChargeMapper.class);

    @Mapping(target = "chargeTypeId", source = "chargeType.id")
    @Mapping(target = "chargeTypeName", source = "chargeType.name")
    FolioChargeDTO toDto(FolioChargeEntity entity);

    @Mapping(target = "voided", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "chargeType", ignore = true)
    FolioChargeEntity toEntity(FolioChargeDTO dto);
}
