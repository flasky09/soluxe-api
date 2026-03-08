package com.hotel_erp.hotel_erp.modules.room;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;

    @GetMapping
    public List<RoomDTO> getAllRooms() {
        return roomService.findAll().stream()
                .map(roomMapper::toDto)
                .toList();
    }

    @PostMapping
    public RoomDTO createRoom(@RequestBody RoomDTO roomDto) {
        RoomEntity roomEntity = roomMapper.toEntity(roomDto);
        return roomMapper.toDto(roomService.save(roomEntity));
    }

    @GetMapping("/{roomIdentifier}")
    public RoomDTO getRoomById(@PathVariable Long roomIdentifier) {
        return roomService.findById(roomIdentifier)
                .map(roomMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }
}
