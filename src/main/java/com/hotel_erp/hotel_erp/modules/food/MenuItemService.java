package com.hotel_erp.hotel_erp.modules.food;

import java.util.List;
import java.util.Optional;

public interface MenuItemService {

    List<MenuItemEntity> findAll();

    Optional<MenuItemEntity> findById(Long id);

    MenuItemEntity save(MenuItemEntity item);

    void deleteById(Long id);

}
