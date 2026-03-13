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
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @PostMapping
    public MaintenanceIssueTypeDTO create(@RequestBody MaintenanceIssueTypeDTO dto) {
        MaintenanceIssueTypeEntity entity = new MaintenanceIssueTypeEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity = repository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }
}
