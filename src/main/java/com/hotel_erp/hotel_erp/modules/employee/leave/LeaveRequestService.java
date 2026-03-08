package com.hotel_erp.hotel_erp.modules.employee.leave;

import java.util.List;
import java.util.Optional;

public interface LeaveRequestService {
    List<LeaveRequestEntity> findByEmployeeId(Long employeeId);
    Optional<LeaveRequestEntity> findById(Long id);
    LeaveRequestEntity save(LeaveRequestEntity request);
    void deleteById(Long id);
}
