package com.hotel_erp.hotel_erp.modules.finance;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance/petty-cash")
@RequiredArgsConstructor
public class PettyCashController {
    private final PettyCashService service;

    @GetMapping
    public List<PettyCashDTO> getAll() {
        return service.getAllEntries();
    }

    @PostMapping
    public PettyCashDTO create(@RequestBody PettyCashDTO dto) {
        return service.createEntry(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteEntry(id);
    }
}
