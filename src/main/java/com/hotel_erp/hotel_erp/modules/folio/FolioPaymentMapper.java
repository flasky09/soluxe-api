package com.hotel_erp.hotel_erp.modules.folio;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface FolioPaymentMapper {
    @Mapping(target = "paymentMethodName", ignore = true)
    FolioPaymentDTO toDto(FolioPaymentEntity entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recordedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "paymentMethod", ignore = true)
    FolioPaymentEntity toEntity(FolioPaymentDTO dto);
}
