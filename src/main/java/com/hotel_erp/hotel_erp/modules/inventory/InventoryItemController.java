package com.hotel_erp.hotel_erp.modules.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/inventory-items", "/api/inventory"})
@RequiredArgsConstructor
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    @GetMapping
    public List<InventoryItemDTO> getAllItems() {
        return inventoryItemService.findAllItems();
    }

    @PostMapping
    public InventoryItemDTO createItem(@RequestBody InventoryItemDTO itemDto) {
        return inventoryItemService.saveItem(itemDto);
    }

    @PutMapping("/{id}")
    public InventoryItemDTO updateItem(@PathVariable Long id, @RequestBody InventoryItemDTO itemDto) {
        itemDto.setId(id);
        return inventoryItemService.saveItem(itemDto);
    }

    @GetMapping("/{id}")
    public InventoryItemDTO getItemById(@PathVariable Long id) {
        return inventoryItemService.findItemById(id)
                .orElseThrow(() -> new RuntimeException("InventoryItem not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        inventoryItemService.deleteById(id);
    }
}
