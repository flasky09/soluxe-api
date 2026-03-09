package com.hotel_erp.hotel_erp.modules.food;

import java.util.List;
import java.util.Optional;

public interface DiningSessionService {
    Optional<DiningSessionEntity> findById(Long id);
    DiningSessionEntity save(DiningSessionEntity session);
    DiningSessionEntity closeSession(Long id);
    void deleteById(Long id);
    List<DiningSessionEntity> findAll();
    List<DiningSessionEntity> findActive();
}
