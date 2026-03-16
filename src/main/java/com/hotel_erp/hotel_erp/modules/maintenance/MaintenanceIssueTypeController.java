package com.hotel_erp.hotel_erp.modules.maintenance;

import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/maintenance-issue-types")
@RequiredArgsConstructor
public class MaintenanceIssueTypeController {

    private final MaintenanceIssueTypeRepository repository;

    @GetMapping
    public List<MaintenanceIssueTypeDTO> getAll() {
        return repository.findAll().stream()
                .map(entity -> {
                    MaintenanceIssueTypeDTO dto = new MaintenanceIssueTypeDTO();
                    dto.setId(entity.getId());
                    dto.setName(entity.getName());
                    dto.setActive(entity.isActive());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'MANAGER')")
    public MaintenanceIssueTypeDTO create(@RequestBody MaintenanceIssueTypeDTO dto) {
        MaintenanceIssueTypeEntity entity = new MaintenanceIssueTypeEntity();
        entity.setName(dto.getName());
        entity.setActive(dto.isActive());
        entity = repository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'MANAGER')")
    public MaintenanceIssueTypeDTO update(@PathVariable Long id, @RequestBody MaintenanceIssueTypeDTO dto) {
        MaintenanceIssueTypeEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance Issue Type not found"));
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
