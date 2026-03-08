package com.hotel_erp.hotel_erp.modules.food;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiningOrderRepository extends BaseRepository<DiningOrderEntity, Long> {
    List<DiningOrderEntity> findBySessionId(Long sessionId);
}
