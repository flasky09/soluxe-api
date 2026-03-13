package com.hotel_erp.hotel_erp.modules.employee.attendance;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttendanceRecordMapper {
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "recordedBy.id", target = "recordedById")
    AttendanceRecordDTO toDto(AttendanceRecordEntity entity);

    @Mapping(source = "employeeId", target = "employee.id")
    @Mapping(source = "recordedById", target = "recordedBy.id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AttendanceRecordEntity toEntity(AttendanceRecordDTO dto);
}
