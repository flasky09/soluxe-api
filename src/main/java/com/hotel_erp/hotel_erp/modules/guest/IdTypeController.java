package com.hotel_erp.hotel_erp.modules.guest;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/id-types")
public class IdTypeController {

    @GetMapping
    public List<IdTypeDTO> getAll() {
        return Arrays.stream(IdType.values())
                .map(type -> {
                    IdTypeDTO dto = new IdTypeDTO();
                    dto.setId((long) type.ordinal() + 1);
                    dto.setName(type.name());
                    dto.setDescription(type.name().replace("_", " "));
                    dto.setActive(true);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
