package com.hotel_erp.hotel_erp.modules.room;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PutMapping("/{id}")
    public RoomDTO updateRoom(@PathVariable Long id, @RequestBody RoomDTO roomDto) {
        RoomEntity roomEntity = roomMapper.toEntity(roomDto);
        roomEntity.setId(id);
        return roomMapper.toDto(roomService.save(roomEntity));
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteById(id);
    }

    @GetMapping("/{roomIdentifier}")
    public RoomDTO getRoomById(@PathVariable Long roomIdentifier) {
        return roomService.findById(roomIdentifier)
                .map(roomMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }
}
