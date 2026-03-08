package com.hotel_erp.hotel_erp.modules.food;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;

    @Override
    public Optional<MenuItemEntity> findById(Long id) {
        return menuItemRepository.findById(id);
    }

    @Override
    public MenuItemEntity save(MenuItemEntity item) {
        return menuItemRepository.save(item);
    }

    @Override
    public void deleteById(Long id) {
        menuItemRepository.deleteById(id);
    }
}
