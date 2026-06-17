package com.hotel_erp.hotel_erp.modules.employee;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import org.springframework.beans.factory.annotation.Autowired;
import com.hotel_erp.hotel_erp.modules.user.UserRepository;

@Mapper(componentModel = "spring")
public abstract class EmployeeMapper {

    @Autowired
    protected UserRepository userRepository;

    @Mapping(source = "department.id", target = "departmentId")
    public abstract EmployeeDTO toDto(EmployeeEntity entity);

    @org.mapstruct.AfterMapping
    protected void resolveAuditNames(EmployeeEntity entity, @org.mapstruct.MappingTarget EmployeeDTO dto) {
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
    @Mapping(source = "departmentId", target = "department.id")
    @Mapping(target = "idType", ignore = true)
    public abstract EmployeeEntity toEntity(EmployeeDTO dto);
}
