package com.hotel_erp.hotel_erp.modules.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory-categories")
@RequiredArgsConstructor
public class InventoryCategoryController {

    private final InventoryCategoryRepository inventoryCategoryRepository;

    @PostMapping
    public InventoryCategoryEntity createCategory(@RequestBody InventoryCategoryEntity category) {
        return inventoryCategoryRepository.save(category);
    }

    @GetMapping("/{id}")
    public InventoryCategoryEntity getCategoryById(@PathVariable Long id) {
        return inventoryCategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("InventoryCategory not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        inventoryCategoryRepository.deleteById(id);
    }
}
