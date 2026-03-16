package com.hotel_erp.hotel_erp.modules.stay;

import com.hotel_erp.hotel_erp.shared.BaseService;

public interface StayService extends BaseService<StayEntity, Long> {
    StayDTO checkIn(Long reservationId, Long roomId, Long userId);
    StayDTO directCheckIn(Long guestId, Long roomId, Integer adults, Integer children, java.time.LocalDate dateOut, Long userId);
    StayDTO checkOut(Long stayId, Long userId);
    StayDTO checkOutByReservationId(Long reservationId, Long userId);
    
    java.util.List<StayDTO> findActiveStays();
    java.util.List<StayDTO> findByGuestId(Long guestId);
    StayDTO extendStay(Long id, java.time.LocalDateTime newDateOut, Long userId);
    void markNoShow(Long reservationId, Long userId);
    void voidStay(Long stayId, Long userId);
    void autoFlagOverstays();
}
