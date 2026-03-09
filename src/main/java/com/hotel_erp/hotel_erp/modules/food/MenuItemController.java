package com.hotel_erp.hotel_erp.modules.food;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;
    private final MenuItemMapper menuItemMapper;

    @GetMapping
    public List<MenuItemDTO> getAllMenuItems() {
        return menuItemService.findAll().stream()
                .map(menuItemMapper::toDto)
                .toList();
    }

    @PostMapping
    public MenuItemDTO createMenuItem(@RequestBody MenuItemDTO itemDto) {
        MenuItemEntity entity = menuItemMapper.toEntity(itemDto);
        return menuItemMapper.toDto(menuItemService.save(entity));
    }

    @PutMapping("/{id}")
    public MenuItemDTO updateMenuItem(@PathVariable Long id, @RequestBody MenuItemDTO itemDto) {
        MenuItemEntity entity = menuItemMapper.toEntity(itemDto);
        entity.setId(id);
        return menuItemMapper.toDto(menuItemService.save(entity));
    }

    @GetMapping("/{id}")
    public MenuItemDTO getMenuItemById(@PathVariable Long id) {
        return menuItemService.findById(id)
                .map(menuItemMapper::toDto)
                .orElseThrow(() -> new RuntimeException("MenuItem not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteById(id);
    }
}
