package com.hotel_erp.hotel_erp.modules.food;

import java.util.List;
import java.util.Optional;

public interface MenuCategoryService {
    Optional<MenuCategoryEntity> findById(Long id);
    MenuCategoryEntity save(MenuCategoryEntity category);
    void deleteById(Long id);
}
