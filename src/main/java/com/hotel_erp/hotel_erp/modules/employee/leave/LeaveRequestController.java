package com.hotel_erp.hotel_erp.modules.employee.leave;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @GetMapping("/employee/{employeeId}")
    public List<LeaveRequestEntity> getRequestsByEmployee(@PathVariable Long employeeId) {
        return leaveRequestService.findByEmployeeId(employeeId);
    }

    @PostMapping
    public LeaveRequestEntity createRequest(@RequestBody LeaveRequestEntity request) {
        return leaveRequestService.save(request);
    }

    @GetMapping("/{id}")
    public LeaveRequestEntity getRequestById(@PathVariable Long id) {
        return leaveRequestService.findById(id).orElseThrow(() -> new RuntimeException("LeaveRequest not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteRequest(@PathVariable Long id) {
        leaveRequestService.deleteById(id);
    }
}
