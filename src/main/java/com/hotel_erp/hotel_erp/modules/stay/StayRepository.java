package com.hotel_erp.hotel_erp.modules.stay;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface StayRepository extends BaseRepository<StayEntity, Long> {

    List<StayEntity> findAllByStatusIn(List<StayStatus> statuses);

    List<StayEntity> findAllByStatusAndDateOutBefore(StayStatus status, java.time.LocalDateTime dateTime);

    long countByRoomIdAndStatus(Long roomId, StayStatus status);

    long countByStatusIn(java.util.List<StayStatus> statuses);

    long countByGuestIdAndStatusIn(Long guestId, List<StayStatus> statuses);

    java.util.List<StayEntity> findAllByGuestIdOrderByDateInDesc(Long guestId);

    List<StayEntity> findByReservationIdAndStatusIn(Long reservationId, List<StayStatus> statuses);

    java.util.Optional<StayEntity> findByReservationIdAndStatus(Long reservationId, StayStatus status);

    List<StayEntity> findAllByRoomId(Long roomId);

    @Query("SELECT s.checkedInBy as userId, COUNT(s) as total FROM StayEntity s WHERE s.checkedInBy IS NOT NULL GROUP BY s.checkedInBy")
    List<Map<String, Object>> countCheckInsByUser();

    @Query("SELECT s.checkedOutBy as userId, COUNT(s) as total FROM StayEntity s WHERE s.checkedOutBy IS NOT NULL GROUP BY s.checkedOutBy")
    List<Map<String, Object>> countCheckOutsByUser();

    @Query("SELECT s.checkedInBy as userId, COUNT(s) as total FROM StayEntity s "
            + "WHERE s.checkedInBy IS NOT NULL AND s.dateIn >= :start AND s.dateIn <= :end "
            + "GROUP BY s.checkedInBy")
    List<Map<String, Object>> countCheckInsByUserInRange(@Param("start") java.time.LocalDateTime start, 
                                                         @Param("end") java.time.LocalDateTime end);

    @Query("SELECT s.checkedOutBy as userId, COUNT(s) as total FROM StayEntity s "
            + "WHERE s.checkedOutBy IS NOT NULL AND s.dateOut >= :start AND s.dateOut <= :end "
            + "GROUP BY s.checkedOutBy")
    List<Map<String, Object>> countCheckOutsByUserInRange(@Param("start") java.time.LocalDateTime start, 
                                                          @Param("end") java.time.LocalDateTime end);

    @Query("SELECT COUNT(s) FROM StayEntity s WHERE s.status = 'ACTIVE' AND s.dateIn <= :end AND (s.dateOut IS NULL OR s.dateOut >= :start)")
    long countOccupiedInRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
