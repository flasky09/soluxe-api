package com.hotel_erp.hotel_erp.modules.employee.attendance;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance-records")
@RequiredArgsConstructor
public class AttendanceRecordController {

    private final AttendanceRecordService attendanceRecordService;

    @GetMapping("/employee/{employeeId}")
    public List<AttendanceRecordEntity> getRecordsByEmployee(@PathVariable Long employeeId) {
        return attendanceRecordService.findByEmployeeId(employeeId);
    }

    @PostMapping
    public AttendanceRecordEntity createRecord(@RequestBody AttendanceRecordEntity record) {
        return attendanceRecordService.save(record);
    }

    @GetMapping("/{id}")
    public AttendanceRecordEntity getRecordById(@PathVariable Long id) {
        return attendanceRecordService.findById(id).orElseThrow(() -> new RuntimeException("AttendanceRecord not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteRecord(@PathVariable Long id) {
        attendanceRecordService.deleteById(id);
    }
}
