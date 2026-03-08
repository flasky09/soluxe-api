package com.hotel_erp.hotel_erp.modules.housekeeping;

import com.hotel_erp.hotel_erp.modules.room.RoomEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/housekeeping")
@RequiredArgsConstructor
public class HousekeepingController {
    private final HousekeepingService housekeepingService;

    @GetMapping("/dirty-rooms")
    public ResponseEntity<List<RoomEntity>> getRoomsNeedingAttention() {
        return ResponseEntity.ok(housekeepingService.getRoomsNeedingAttention());
    }

    @PostMapping("/log-action")
    public ResponseEntity<HousekeepingLogEntity> logAction(@Valid @RequestBody HousekeepingActionDTO actionDTO) {
        return ResponseEntity.ok(housekeepingService.logAction(actionDTO));
    }
}
