package com.hotel_erp.hotel_erp.modules.guest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/id-types")
@RequiredArgsConstructor
public class IdTypeController {

    private final IdentityTypeRepository repository;

    @GetMapping
    public List<IdTypeDTO> getAll() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public IdTypeDTO create(@RequestBody IdTypeDTO dto) {
        IdentityTypeEntity entity = new IdentityTypeEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.isActive());
        entity = repository.save(entity);
        return convertToDto(entity);
    }

    @PutMapping("/{id}")
    public IdTypeDTO update(@PathVariable Long id, @RequestBody IdTypeDTO dto) {
        IdentityTypeEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Identity Type not found"));
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.isActive());
        entity = repository.save(entity);
        return convertToDto(entity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    private IdTypeDTO convertToDto(IdentityTypeEntity entity) {
        IdTypeDTO dto = new IdTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setActive(entity.isActive());
        return dto;
    }
}
