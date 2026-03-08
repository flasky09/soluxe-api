package com.hotel_erp.hotel_erp.modules.employee.attendance;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRecordRepository extends BaseRepository<AttendanceRecordEntity, Long> {
    List<AttendanceRecordEntity> findByEmployeeId(Long employeeId);
}
