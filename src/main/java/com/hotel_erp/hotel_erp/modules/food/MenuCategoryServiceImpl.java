package com.hotel_erp.hotel_erp.modules.food;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuCategoryServiceImpl implements MenuCategoryService {

    private final MenuCategoryRepository menuCategoryRepository;

    @Override
    public Optional<MenuCategoryEntity> findById(Long id) {
        return menuCategoryRepository.findById(id);
    }

    @Override
    public MenuCategoryEntity save(MenuCategoryEntity category) {
        return menuCategoryRepository.save(category);
    }

    @Override
    public void deleteById(Long id) {
        menuCategoryRepository.deleteById(id);
    }
}
