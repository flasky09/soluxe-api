package com.hotel_erp.hotel_erp.modules.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory-categories")
@RequiredArgsConstructor
public class InventoryCategoryController {

    private final InventoryCategoryRepository inventoryCategoryRepository;

    @GetMapping
    public List<InventoryCategoryEntity> getAllCategories() {
        return inventoryCategoryRepository.findAll();
    }

    @PostMapping
    public InventoryCategoryEntity createCategory(@RequestBody InventoryCategoryEntity category) {
        return inventoryCategoryRepository.save(category);
    }

    @PutMapping("/{id}")
    public InventoryCategoryEntity updateCategory(@PathVariable Long id, @RequestBody InventoryCategoryEntity category) {
        category.setId(id);
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
