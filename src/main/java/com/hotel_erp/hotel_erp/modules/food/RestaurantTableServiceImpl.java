package com.hotel_erp.hotel_erp.modules.food;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;

    @Override
    public Optional<RestaurantTableEntity> findById(Long id) {
        return restaurantTableRepository.findById(id);
    }

    @Override
    public RestaurantTableEntity save(RestaurantTableEntity table) {
        return restaurantTableRepository.save(table);
    }

    @Override
    public void deleteById(Long id) {
        restaurantTableRepository.deleteById(id);
    }
}
