package com.hotel_erp.hotel_erp.modules.food;

import java.util.List;
import java.util.Optional;

public interface DiningOrderService {
    List<DiningOrderEntity> findBySessionId(Long sessionId);
    Optional<DiningOrderEntity> findById(Long id);
    DiningOrderEntity save(DiningOrderEntity order);
    void deleteById(Long id);
}
