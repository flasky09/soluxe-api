package com.hotel_erp.hotel_erp.modules.employee.attendance;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attendance-records")
@RequiredArgsConstructor
public class AttendanceRecordController {

    private final AttendanceRecordService attendanceRecordService;
    private final AttendanceRecordMapper attendanceRecordMapper;

    @GetMapping
    public ResponseEntity<List<AttendanceRecordDTO>> getAllRecords() {
        List<AttendanceRecordDTO> list = attendanceRecordService.findAll().stream()
                .map(attendanceRecordMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AttendanceRecordDTO>> getRecordsByEmployee(@PathVariable Long employeeId) {
        List<AttendanceRecordDTO> list = attendanceRecordService.findByEmployeeId(employeeId).stream()
                .map(attendanceRecordMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<AttendanceRecordDTO> createRecord(@RequestBody AttendanceRecordDTO dto) {
        AttendanceRecordEntity entity = attendanceRecordMapper.toEntity(dto);
        AttendanceRecordDTO savedDto = attendanceRecordMapper.toDto(attendanceRecordService.save(entity));
        return ResponseEntity.ok(savedDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceRecordDTO> updateRecord(@PathVariable Long id, @RequestBody AttendanceRecordDTO dto) {
        if (!attendanceRecordService.findById(id).isPresent()) {
            throw new RuntimeException("AttendanceRecord not found");
        }
        AttendanceRecordEntity entity = attendanceRecordMapper.toEntity(dto);
        entity.setId(id);
        AttendanceRecordDTO savedDto = attendanceRecordMapper.toDto(attendanceRecordService.save(entity));
        return ResponseEntity.ok(savedDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceRecordDTO> getRecordById(@PathVariable Long id) {
        return attendanceRecordService.findById(id)
                .map(attendanceRecordMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("AttendanceRecord not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        attendanceRecordService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
