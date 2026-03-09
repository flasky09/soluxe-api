package com.hotel_erp.hotel_erp.modules.stay;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StayRepository extends BaseRepository<StayEntity, Long> {
    List<StayEntity> findAllByStatus(StayStatus status);
    long countByRoomIdAndStatus(Long roomId, StayStatus status);
    java.util.Optional<StayEntity> findByReservationIdAndStatus(Long reservationId, StayStatus status);
}
