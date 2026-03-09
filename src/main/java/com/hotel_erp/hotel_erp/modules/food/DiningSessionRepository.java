package com.hotel_erp.hotel_erp.modules.food;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiningSessionRepository extends BaseRepository<DiningSessionEntity, Long> {
    List<DiningSessionEntity> findByStatus(SessionStatus status);
}
