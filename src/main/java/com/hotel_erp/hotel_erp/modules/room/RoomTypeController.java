package com.hotel_erp.hotel_erp.modules.room;

import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'MANAGER')")
    public ResponseEntity<RoomTypeDTO> createRoomType(@RequestBody RoomTypeDTO roomTypeDTO) {
        RoomTypeEntity entity = roomTypeMapper.toEntity(roomTypeDTO);
        RoomTypeEntity saved = roomTypeService.save(entity);
        return ResponseEntity.ok(roomTypeMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'MANAGER')")
    public RoomTypeDTO update(@PathVariable Long id, @RequestBody RoomTypeDTO dto) {
        RoomTypeEntity entity = roomTypeService.findById(id)
                .orElseThrow(() -> new RuntimeException("Room Type not found"));
        
        entity.setName(dto.getName());
        entity.setDefaultRate(dto.getDefaultRate());
        entity.setWeekendRate(dto.getWeekendRate());
        entity.setCapacity(dto.getCapacity());
        entity.setBedType(dto.getBedType());
        entity.setAmenities(dto.getAmenities());
        
        RoomTypeEntity saved = roomTypeService.save(entity);
        return roomTypeMapper.toDto(saved);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HOTEL_ADMIN')")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Long id) {
        roomTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
