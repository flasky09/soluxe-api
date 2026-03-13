package com.hotel_erp.hotel_erp.modules.employee.leave;

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
                    dto.setDescription(entity.getDescription());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @PostMapping
    public LeaveTypeDTO create(@RequestBody LeaveTypeDTO dto) {
        LeaveTypeEntity entity = new LeaveTypeEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity = repository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }
}
