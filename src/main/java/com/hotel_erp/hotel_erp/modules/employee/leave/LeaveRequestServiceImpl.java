package com.hotel_erp.hotel_erp.modules.employee.leave;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveRequestMapper mapper;

    @Override
    public List<LeaveRequestDTO> findByEmployeeId(Long employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LeaveRequestDTO> findById(Long id) {
        return leaveRequestRepository.findById(id).map(mapper::toDto);
    }

    @Override
    @Transactional
    public LeaveRequestDTO save(LeaveRequestDTO dto) {
        LeaveRequestEntity entity = mapper.toEntity(dto);
        
        if (dto.getLeaveTypeId() != null) {
            leaveTypeRepository.findById(dto.getLeaveTypeId())
                    .ifPresent(entity::setLeaveType);
        }

        entity = leaveRequestRepository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        leaveRequestRepository.deleteById(id);
    }
}
