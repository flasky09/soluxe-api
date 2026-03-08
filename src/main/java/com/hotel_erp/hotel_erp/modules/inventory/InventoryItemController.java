package com.hotel_erp.hotel_erp.modules.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory-items")
@RequiredArgsConstructor
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;
    private final InventoryItemMapper inventoryItemMapper;

    @GetMapping
    public List<InventoryItemDTO> getAllItems() {
        return inventoryItemService.findAll().stream()
                .map(inventoryItemMapper::toDto)
                .toList();
    }

    @PostMapping
    public InventoryItemDTO createItem(@RequestBody InventoryItemDTO itemDto) {
        InventoryItemEntity entity = inventoryItemMapper.toEntity(itemDto);
        return inventoryItemMapper.toDto(inventoryItemService.save(entity));
    }

    @GetMapping("/{id}")
    public InventoryItemDTO getItemById(@PathVariable Long id) {
        return inventoryItemService.findById(id)
                .map(inventoryItemMapper::toDto)
                .orElseThrow(() -> new RuntimeException("InventoryItem not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        inventoryItemService.deleteById(id);
    }
}
