package com.hotel_erp.hotel_erp.modules.employee.leave;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends BaseRepository<LeaveRequestEntity, Long> {
    List<LeaveRequestEntity> findByEmployeeId(Long employeeId);
}
