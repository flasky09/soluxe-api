package com.hotel_erp.hotel_erp.modules.food;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @GetMapping
    public List<MenuItemEntity> getAllMenuItems() {
        return menuItemService.findAll();
    }

    @PostMapping
    public MenuItemEntity createMenuItem(@RequestBody MenuItemEntity item) {
        return menuItemService.save(item);
    }

    @GetMapping("/{id}")
    public MenuItemEntity getMenuItemById(@PathVariable Long id) {
        return menuItemService.findById(id).orElseThrow(() -> new RuntimeException("MenuItem not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteById(id);
    }
}
