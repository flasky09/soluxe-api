package com.hotel_erp.hotel_erp.modules.finance;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/finance/cash-movements")
@RequiredArgsConstructor
public class CashMovementController {
    private final CashMovementService service;

    @GetMapping
    public List<CashMovementDTO> getAll() {
        return service.getAllMovements();
    }

    @PostMapping
    public CashMovementDTO create(@RequestBody CashMovementDTO dto) {
        return service.createMovement(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteMovement(id);
    }
}
