package com.hotel_erp.hotel_erp.modules.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;
    private final SupplierMapper supplierMapper;

    @GetMapping
    public List<SupplierDTO> getAllSuppliers() {
        return supplierService.findAll().stream()
                .map(supplierMapper::toDto)
                .toList();
    }

    @PostMapping
    public SupplierDTO createSupplier(@RequestBody SupplierDTO supplierDto) {
        SupplierEntity entity = supplierMapper.toEntity(supplierDto);
        return supplierMapper.toDto(supplierService.save(entity));
    }

    @GetMapping("/{id}")
    public SupplierDTO getSupplierById(@PathVariable Long id) {
        return supplierService.findById(id)
                .map(supplierMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable Long id) {
        supplierService.deleteById(id);
    }
}
