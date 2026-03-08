package com.hotel_erp.hotel_erp.modules.room;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-types")
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;
    private final RoomTypeMapper roomTypeMapper;

    @GetMapping
    public List<RoomTypeDTO> getAllRoomTypes() {
        return roomTypeService.findAll().stream()
                .map(roomTypeMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeDTO> getRoomTypeById(@PathVariable Long id) {
        return roomTypeService.findById(id)
                .map(roomTypeMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoomTypeDTO> createRoomType(@RequestBody RoomTypeDTO roomTypeDTO) {
        RoomTypeEntity entity = roomTypeMapper.toEntity(roomTypeDTO);
        RoomTypeEntity saved = roomTypeService.save(entity);
        return ResponseEntity.ok(roomTypeMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomTypeDTO> updateRoomType(@PathVariable Long id, @RequestBody RoomTypeDTO roomTypeDTO) {
        return roomTypeService.findById(id)
                .map(existing -> {
                    RoomTypeEntity entity = roomTypeMapper.toEntity(roomTypeDTO);
                    entity.setId(id);
                    RoomTypeEntity updated = roomTypeService.save(entity);
                    return ResponseEntity.ok(roomTypeMapper.toDto(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Long id) {
        roomTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
