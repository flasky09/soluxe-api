package com.hotel_erp.hotel_erp.modules.reservation;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ReservationRepository extends BaseRepository<ReservationEntity, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT COUNT(r) FROM ReservationEntity r WHERE r.status = :status AND r.dateIn = :dateIn")
    long countByStatusAndDateIn(ReservationStatus status, java.time.LocalDate dateIn);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(r) FROM ReservationEntity r WHERE r.status = :status AND r.dateOut = :dateOut")
    long countByStatusAndDateOut(ReservationStatus status, java.time.LocalDate dateOut);
}
