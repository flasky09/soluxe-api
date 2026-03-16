package com.hotel_erp.hotel_erp.modules.folio;

import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/charge-types")
@RequiredArgsConstructor
public class ChargeTypeController {

    private final ChargeTypeRepository repository;

    @GetMapping
    public List<ChargeTypeDTO> getAll() {
        return repository.findAll().stream()
                .map(entity -> {
                    ChargeTypeDTO dto = new ChargeTypeDTO();
                    dto.setId(entity.getId());
                    dto.setName(entity.getName());
                    dto.setActive(entity.isActive());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'MANAGER')")
    public ChargeTypeDTO create(@RequestBody ChargeTypeDTO dto) {
        ChargeTypeEntity entity = new ChargeTypeEntity();
        entity.setName(dto.getName());
        entity.setActive(dto.isActive());
        entity = repository.saveAndFlush(entity);
        dto.setId(entity.getId());
        return dto;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'MANAGER')")
    public ChargeTypeDTO update(@PathVariable Long id, @RequestBody ChargeTypeDTO dto) {
        ChargeTypeEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Charge Type not found"));
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
            // Fallback to logical delete if hard delete fails (e.g. FK constraint)
            repository.findById(id).ifPresent(entity -> {
                entity.setActive(false);
                repository.save(entity);
            });
        }
    }
}
