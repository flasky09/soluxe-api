package com.hotel_erp.hotel_erp.modules.room;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController("roomHousekeepingController")
@RequestMapping("/api/housekeeping")
@RequiredArgsConstructor
public class HousekeepingController {

    private final RoomRepository roomRepository;

    @GetMapping("/dirty")
    public List<RoomEntity> getDirtyRooms() {
        return roomRepository.findAll().stream()
                .filter(r -> r.getStatus() == RoomStatus.DIRTY || r.getStatus() == RoomStatus.CLEANING)
                .toList();
    }

    @PostMapping("/rooms/{id}/status")
    public RoomEntity updateRoomStatus(@PathVariable Long id, @RequestParam RoomStatus status) {
        RoomEntity room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setStatus(status);
        return roomRepository.save(room);
    }
}
