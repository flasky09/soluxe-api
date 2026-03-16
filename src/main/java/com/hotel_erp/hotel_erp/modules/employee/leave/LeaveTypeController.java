package com.hotel_erp.hotel_erp.modules.employee.leave;

import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/leave-types")
@RequiredArgsConstructor
public class LeaveTypeController {

    private final LeaveTypeRepository repository;

    @GetMapping
    public List<LeaveTypeDTO> getAll() {
        return repository.findAll().stream()
                .map(entity -> {
                    LeaveTypeDTO dto = new LeaveTypeDTO();
                    dto.setId(entity.getId());
                    dto.setName(entity.getName());
                    dto.setActive(entity.isActive());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'MANAGER')")
    public LeaveTypeDTO create(@RequestBody LeaveTypeDTO dto) {
        LeaveTypeEntity entity = new LeaveTypeEntity();
        entity.setName(dto.getName());
        entity.setActive(dto.isActive());
        entity = repository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'MANAGER')")
    public LeaveTypeDTO update(@PathVariable Long id, @RequestBody LeaveTypeDTO dto) {
        LeaveTypeEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave Type not found"));
        entity.setName(dto.getName());
        entity.setActive(dto.isActive());
        repository.save(entity);
        return dto;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HOTEL_ADMIN')")
    public void delete(@PathVariable Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            // Fallback to logical delete
            repository.findById(id).ifPresent(entity -> {
                entity.setActive(false);
                repository.save(entity);
            });
        }
    }
}
