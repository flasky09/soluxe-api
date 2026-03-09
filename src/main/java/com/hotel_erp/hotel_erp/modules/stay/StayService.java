package com.hotel_erp.hotel_erp.modules.stay;

import com.hotel_erp.hotel_erp.shared.BaseService;

public interface StayService extends BaseService<StayEntity, Long> {
    StayDTO checkIn(Long reservationId, Long roomId, Long userId);
    StayDTO checkOut(Long stayId, Long userId);
    StayDTO checkOutByReservationId(Long reservationId, Long userId);
}
