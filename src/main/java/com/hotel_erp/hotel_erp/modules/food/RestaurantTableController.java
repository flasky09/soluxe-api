package com.hotel_erp.hotel_erp.modules.food;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant-tables")
@RequiredArgsConstructor
public class RestaurantTableController {

    private final RestaurantTableService restaurantTableService;

    @PostMapping
    public RestaurantTableEntity createTable(@RequestBody RestaurantTableEntity table) {
        return restaurantTableService.save(table);
    }

    @GetMapping("/{id}")
    public RestaurantTableEntity getTableById(@PathVariable Long id) {
        return restaurantTableService.findById(id).orElseThrow(() -> new RuntimeException("RestaurantTable not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteTable(@PathVariable Long id) {
        restaurantTableService.deleteById(id);
    }
}
