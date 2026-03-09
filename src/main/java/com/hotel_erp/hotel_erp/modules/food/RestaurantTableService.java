package com.hotel_erp.hotel_erp.modules.food;

import java.util.List;
import java.util.Optional;

public interface RestaurantTableService {
    Optional<RestaurantTableEntity> findById(Long id);
    List<RestaurantTableEntity> findAll();
    RestaurantTableEntity save(RestaurantTableEntity table);
    void deleteById(Long id);
}
