package com.hotel_erp.hotel_erp.modules.food;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-categories")
@RequiredArgsConstructor
public class MenuCategoryController {

    private final MenuCategoryService menuCategoryService;

    @PostMapping
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
    public void deleteCategory(@PathVariable Long id) {
        menuCategoryService.deleteById(id);
    }
}
