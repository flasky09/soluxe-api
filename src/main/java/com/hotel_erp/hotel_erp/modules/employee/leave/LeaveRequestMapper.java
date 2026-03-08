package com.hotel_erp.hotel_erp.modules.employee.leave;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LeaveRequestMapper {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "approvedBy.id", target = "approvedById")
    LeaveRequestDTO toDto(LeaveRequestEntity entity);

    @Mapping(source = "employeeId", target = "employee.id")
    @Mapping(source = "approvedById", target = "approvedBy.id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LeaveRequestEntity toEntity(LeaveRequestDTO dto);
}
