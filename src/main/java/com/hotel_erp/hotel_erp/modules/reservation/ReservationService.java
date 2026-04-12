package com.hotel_erp.hotel_erp.modules.reservation;

import com.hotel_erp.hotel_erp.shared.BaseService;

public interface ReservationService extends com.hotel_erp.hotel_erp.shared.BaseService<ReservationEntity, Long> {
    ReservationDTO createFromDto(ReservationDTO dto);

    ReservationDTO createFromDto(ReservationDTO dto, Long userId);

    ReservationDTO updateReservation(Long id, ReservationDTO dto);

    ReservationDTO updateReservation(Long id, ReservationDTO dto, Long userId);

    ReservationDTO cancel(Long reservationId);

    ReservationDTO cancel(Long reservationId, Long userId);

    java.util.List<ReservationDTO> getTodayArrivals();
}
