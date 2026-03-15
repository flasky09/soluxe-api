package com.hotel_erp.hotel_erp.modules.maintenance;

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
                    dto.setDescription(entity.getDescription());
                    dto.setActive(entity.isActive());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @PostMapping
    public MaintenanceIssueTypeDTO create(@RequestBody MaintenanceIssueTypeDTO dto) {
        MaintenanceIssueTypeEntity entity = new MaintenanceIssueTypeEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.isActive());
        entity = repository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    @PutMapping("/{id}")
    public MaintenanceIssueTypeDTO update(@PathVariable Long id, @RequestBody MaintenanceIssueTypeDTO dto) {
        MaintenanceIssueTypeEntity entity = repository.findById(id).orElseThrow();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.isActive());
        repository.save(entity);
        return dto;
    }

    @DeleteMapping("/{id}")
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
