package com.hotel_erp.hotel_erp.modules.shift;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftHandoverRepository extends JpaRepository<ShiftHandoverEntity, Long> {
    Optional<ShiftHandoverEntity> findByUserIdAndStatus(Long userId, ShiftHandoverEntity.ShiftStatus status);
    
    List<ShiftHandoverEntity> findAllByOrderByClockInTimeDesc();
}
