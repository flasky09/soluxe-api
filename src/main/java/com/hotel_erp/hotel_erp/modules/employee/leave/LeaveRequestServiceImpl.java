package com.hotel_erp.hotel_erp.modules.employee.leave;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;

    @Override
    public List<LeaveRequestEntity> findByEmployeeId(Long employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId);
    }

    @Override
    public Optional<LeaveRequestEntity> findById(Long id) {
        return leaveRequestRepository.findById(id);
    }

    @Override
    public LeaveRequestEntity save(LeaveRequestEntity request) {
        return leaveRequestRepository.save(request);
    }

    @Override
    public void deleteById(Long id) {
        leaveRequestRepository.deleteById(id);
    }
}
