package com.hotel_erp.hotel_erp.modules.employee.leave;

import java.util.List;
import java.util.Optional;

public interface LeaveRequestService {
    List<LeaveRequestDTO> findAll();
    List<LeaveRequestDTO> findByEmployeeId(Long employeeId);
    Optional<LeaveRequestDTO> findById(Long id);
    LeaveRequestDTO save(LeaveRequestDTO dto);
    void deleteById(Long id);
}
