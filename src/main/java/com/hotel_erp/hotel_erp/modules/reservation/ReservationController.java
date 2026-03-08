package com.hotel_erp.hotel_erp.modules.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @GetMapping
    public List<ReservationDTO> getAllReservations() {
        return reservationService.findAll().stream()
                .map(reservationMapper::toDto)
                .toList();
    }

    @PostMapping("/{reservationIdentifier}/check-in")
    public ReservationDTO checkInReservation(@PathVariable Long reservationIdentifier, @RequestParam Long roomId) {
        return reservationService.checkIn(reservationIdentifier, roomId);
    }

    @PostMapping("/{reservationIdentifier}/check-out")
    public ReservationDTO checkOutReservation(@PathVariable Long reservationIdentifier) {
        return reservationService.checkOut(reservationIdentifier);
    }

    @PostMapping("/{reservationIdentifier}/cancel")
    public ReservationDTO cancelReservation(@PathVariable Long reservationIdentifier) {
        return reservationService.cancel(reservationIdentifier);
    }

    @PostMapping
    public ReservationDTO createReservation(@RequestBody ReservationDTO reservationDTO) {
        return reservationService.createFromDto(reservationDTO);
    }

    @GetMapping("/{reservationIdentifier}")
    public ReservationDTO getReservationById(@PathVariable Long reservationIdentifier) {
        ReservationEntity reservation = reservationService.findById(reservationIdentifier)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        return reservationMapper.toDto(reservation);
    }
}
