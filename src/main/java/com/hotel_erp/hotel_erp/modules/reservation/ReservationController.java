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

    @GetMapping("/arrivals")
    public List<ReservationDTO> getTodayArrivals() {
        return reservationService.getTodayArrivals();
    }


    @PostMapping("/{reservationIdentifier}/cancel")
    public ReservationDTO cancelReservation(@PathVariable Long reservationIdentifier, @RequestParam("userId") Long userId) {
        return reservationService.cancel(reservationIdentifier, userId);
    }

    @PostMapping
    public ReservationDTO createReservation(@RequestBody ReservationDTO reservationDTO, @RequestParam("userId") Long userId) {
        return reservationService.createFromDto(reservationDTO, userId);
    }

    @PutMapping("/{reservationIdentifier}")
    public ReservationDTO updateReservation(@PathVariable Long reservationIdentifier,
                                            @RequestBody ReservationDTO reservationDTO,
                                            @RequestParam("userId") Long userId) {
        return reservationService.updateReservation(reservationIdentifier, reservationDTO, userId);
    }

    @GetMapping("/{reservationIdentifier}")
    public ReservationDTO getReservationById(@PathVariable Long reservationIdentifier) {
        ReservationEntity reservation = reservationService.findById(reservationIdentifier)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        return reservationMapper.toDto(reservation);
    }
}
