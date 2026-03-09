package com.hotel_erp.hotel_erp.modules.food;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant-tables")
@RequiredArgsConstructor
public class RestaurantTableController {

    private final RestaurantTableService restaurantTableService;
    private final RestaurantTableMapper restaurantTableMapper;

    @GetMapping
    public List<RestaurantTableDTO> getAllTables() {
        return restaurantTableService.findAll().stream()
                .map(restaurantTableMapper::toDto)
                .toList();
    }

    @PostMapping
    public RestaurantTableDTO createTable(@RequestBody RestaurantTableDTO tableDto) {
        RestaurantTableEntity entity = restaurantTableMapper.toEntity(tableDto);
        return restaurantTableMapper.toDto(restaurantTableService.save(entity));
    }

    @PutMapping("/{id}")
    public RestaurantTableDTO updateTable(@PathVariable Long id, @RequestBody RestaurantTableDTO tableDto) {
        RestaurantTableEntity entity = restaurantTableMapper.toEntity(tableDto);
        entity.setId(id);
        return restaurantTableMapper.toDto(restaurantTableService.save(entity));
    }

    @GetMapping("/{id}")
    public RestaurantTableDTO getTableById(@PathVariable Long id) {
        return restaurantTableService.findById(id)
                .map(restaurantTableMapper::toDto)
                .orElseThrow(() -> new RuntimeException("RestaurantTable not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteTable(@PathVariable Long id) {
        restaurantTableService.deleteById(id);
    }
}
