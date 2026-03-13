package com.hotel_erp.hotel_erp.modules.employee;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(target = "idTypeId", source = "idType.id")
    @Mapping(target = "idTypeName", source = "idType.name")
    EmployeeDTO toDto(EmployeeEntity entity);

    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "departmentId", target = "department.id")
    @Mapping(target = "idType", ignore = true)
    EmployeeEntity toEntity(EmployeeDTO dto);
}
