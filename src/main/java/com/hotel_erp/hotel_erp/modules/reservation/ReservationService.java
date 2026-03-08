package com.hotel_erp.hotel_erp.modules.reservation;

import com.hotel_erp.hotel_erp.shared.BaseService;

public interface ReservationService extends com.hotel_erp.hotel_erp.shared.BaseService<ReservationEntity, Long> {
    ReservationDTO createFromDto(ReservationDTO dto);
    ReservationDTO checkIn(Long reservationId, Long roomId);
    ReservationDTO checkOut(Long reservationId);
    ReservationDTO cancel(Long reservationId);
}
