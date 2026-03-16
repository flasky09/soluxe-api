package com.hotel_erp.hotel_erp.modules.food;

import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-categories")
@RequiredArgsConstructor
public class MenuCategoryController {

    private final MenuCategoryService menuCategoryService;

    @PostMapping
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'MANAGER')")
    public MenuCategoryEntity createCategory(@RequestBody MenuCategoryEntity category) {
        return menuCategoryService.save(category);
    }

    @GetMapping
    public List<MenuCategoryEntity> getAllCategories() {
        return menuCategoryService.findAll();
    }

    @GetMapping("/{id}")
    public MenuCategoryEntity getCategoryById(@PathVariable Long id) {
        return menuCategoryService.findById(id).orElseThrow(() -> new RuntimeException("MenuCategory not found"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HOTEL_ADMIN')")
    public void deleteCategory(@PathVariable Long id) {
        menuCategoryService.deleteById(id);
    }
}
