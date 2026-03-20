package com.hotel_erp.hotel_erp.modules.employee.attendance;

import java.util.List;
import java.util.Optional;

public interface AttendanceRecordService {

    List<AttendanceRecordEntity> findAll();

    List<AttendanceRecordEntity> findByEmployeeId(Long employeeId);

    Optional<AttendanceRecordEntity> findById(Long id);

    AttendanceRecordEntity save(AttendanceRecordEntity record);

    void deleteById(Long id);

}
