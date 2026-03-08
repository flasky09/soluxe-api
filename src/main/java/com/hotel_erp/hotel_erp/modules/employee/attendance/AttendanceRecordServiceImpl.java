package com.hotel_erp.hotel_erp.modules.employee.attendance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceRecordServiceImpl implements AttendanceRecordService {

    private final AttendanceRecordRepository attendanceRecordRepository;

    @Override
    public List<AttendanceRecordEntity> findByEmployeeId(Long employeeId) {
        return attendanceRecordRepository.findByEmployeeId(employeeId);
    }

    @Override
    public Optional<AttendanceRecordEntity> findById(Long id) {
        return attendanceRecordRepository.findById(id);
    }

    @Override
    public AttendanceRecordEntity save(AttendanceRecordEntity record) {
        return attendanceRecordRepository.save(record);
    }

    @Override
    public void deleteById(Long id) {
        attendanceRecordRepository.deleteById(id);
    }
}
