package com.hotel_erp.hotel_erp.modules.reservation;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends BaseRepository<ReservationEntity, Long> {
    @Query("SELECT r FROM ReservationEntity r WHERE r.status = :status AND r.dateIn = :dateIn")
    List<ReservationEntity> findByStatusAndDateIn(ReservationStatus status, LocalDate dateIn);

    List<ReservationEntity> findByStatus(ReservationStatus status);

    @Query("SELECT r FROM ReservationEntity r WHERE r.status = :status AND r.dateIn <= :dateIn")
    List<ReservationEntity> findByStatusAndDateInLessThanEqual(ReservationStatus status, LocalDate dateIn);

    @Query("SELECT COUNT(r) FROM ReservationEntity r WHERE r.status = :status AND r.dateIn <= :dateIn")
    long countByStatusAndDateInLessThanEqual(ReservationStatus status, LocalDate dateIn);

    Optional<ReservationEntity> findByRoomIdAndStatus(Long roomId, ReservationStatus status);

    boolean existsByGuestIdAndStatus(Long guestId, ReservationStatus status);

    @Query("SELECT COUNT(r) FROM ReservationEntity r WHERE r.status = :status AND r.dateIn = :dateIn")
    long countByStatusAndDateIn(ReservationStatus status, LocalDate dateIn);

    @Query("SELECT COUNT(r) FROM ReservationEntity r WHERE r.status = :status AND r.dateOut = :dateOut")
    long countByStatusAndDateOut(ReservationStatus status, LocalDate dateOut);
}
