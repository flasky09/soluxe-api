package com.hotel_erp.hotel_erp.modules.folio;

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
                    dto.setDescription(entity.getDescription());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @PostMapping
    public ChargeTypeDTO create(@RequestBody ChargeTypeDTO dto) {
        ChargeTypeEntity entity = new ChargeTypeEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity = repository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }
}
