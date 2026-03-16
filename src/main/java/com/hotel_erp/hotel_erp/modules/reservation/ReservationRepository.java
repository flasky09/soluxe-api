package com.hotel_erp.hotel_erp.modules.reservation;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends BaseRepository<ReservationEntity, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT r FROM ReservationEntity r WHERE r.status = :status AND r.dateIn = :dateIn")
    List<ReservationEntity> findByStatusAndDateIn(ReservationStatus status, LocalDate dateIn);

    List<ReservationEntity> findByStatus(ReservationStatus status);

    @org.springframework.data.jpa.repository.Query("SELECT r FROM ReservationEntity r WHERE r.status = :status AND r.dateIn <= :dateIn")
    java.util.List<ReservationEntity> findByStatusAndDateInLessThanEqual(ReservationStatus status, java.time.LocalDate dateIn);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(r) FROM ReservationEntity r WHERE r.status = :status AND r.dateIn <= :dateIn")
    long countByStatusAndDateInLessThanEqual(ReservationStatus status, java.time.LocalDate dateIn);
    Optional<ReservationEntity> findByRoomIdAndStatus(Long roomId, ReservationStatus status);
    boolean existsByGuestIdAndStatus(Long guestId, ReservationStatus status);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(r) FROM ReservationEntity r WHERE r.status = :status AND r.dateIn = :dateIn")
    long countByStatusAndDateIn(ReservationStatus status, java.time.LocalDate dateIn);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(r) FROM ReservationEntity r WHERE r.status = :status AND r.dateOut = :dateOut")
    long countByStatusAndDateOut(ReservationStatus status, java.time.LocalDate dateOut);
}
