package com.hotel_erp.hotel_erp.modules.shift;

import com.hotel_erp.hotel_erp.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftHandoverController {

    private final ShiftHandoverService shiftHandoverService;

    @PostMapping("/clock-in")
    public ResponseEntity<ShiftHandoverDTO> clockIn(Authentication authentication, @RequestBody Map<String, String> request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String shiftType = request.get("shiftType");
        String employeeId = request.get("employeeId");
        return ResponseEntity.ok(shiftHandoverService.clockIn(userDetails.getId(), shiftType, employeeId));
    }

    @PostMapping("/clock-out/{id}")
    public ResponseEntity<ShiftHandoverDTO> clockOut(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String notes = request.get("notes");
        return ResponseEntity.ok(shiftHandoverService.clockOut(id, notes));
    }

    @GetMapping("/current")
    public ResponseEntity<ShiftHandoverDTO> getCurrentShift(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return shiftHandoverService.getActiveShift(userDetails.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/history")
    public ResponseEntity<List<ShiftHandoverDTO>> getShiftHistory() {
        return ResponseEntity.ok(shiftHandoverService.getAllShifts());
    }
}
