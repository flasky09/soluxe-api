package com.hotel_erp.hotel_erp.modules.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory-units")
@RequiredArgsConstructor
public class InventoryUnitController {

    private final InventoryUnitRepository repository;

    @GetMapping
    public List<InventoryUnitDTO> getAll() {
        return repository.findAll().stream()
                .map(entity -> {
                    InventoryUnitDTO dto = new InventoryUnitDTO();
                    dto.setId(entity.getId());
                    dto.setName(entity.getName());
                    dto.setDescription(entity.getDescription());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @PostMapping
    public InventoryUnitDTO create(@RequestBody InventoryUnitDTO dto) {
        InventoryUnitEntity entity = new InventoryUnitEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity = repository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }
}
