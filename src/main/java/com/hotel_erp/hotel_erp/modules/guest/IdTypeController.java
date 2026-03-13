package com.hotel_erp.hotel_erp.modules.guest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/id-types")
@RequiredArgsConstructor
public class IdTypeController {

    private final IdTypeRepository repository;

    @GetMapping
    public List<IdTypeDTO> getAll() {
        return repository.findAll().stream()
                .map(entity -> {
                    IdTypeDTO dto = new IdTypeDTO();
                    dto.setId(entity.getId());
                    dto.setName(entity.getName());
                    dto.setDescription(entity.getDescription());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @PostMapping
    public IdTypeDTO create(@RequestBody IdTypeDTO dto) {
        IdTypeEntity entity = new IdTypeEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity = repository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }
}
