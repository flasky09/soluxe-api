package com.hotel_erp.hotel_erp.modules.stay;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stays")
@RequiredArgsConstructor
public class StayController {

    private final StayService stayService;
    private final StayMapper stayMapper;

    @GetMapping
    public java.util.List<StayDTO> getAllStays() {
        return stayService.findAll().stream()
                .map(stayMapper::toDto)
                .toList();
    }

    @PostMapping("/check-in")
    public StayDTO checkIn(@RequestBody CheckInRequest request) {
        return stayService.checkIn(request.getReservationId(), request.getRoomId(), request.getUserId());
    }

    @PostMapping("/walk-in")
    public StayDTO walkIn(@RequestBody WalkInRequest request) {
        return stayService.walkInCheckIn(request.getGuestId(), request.getRoomId(), request.getAdults(), request.getChildren(), request.getDateOut(), request.getUserId());
    }

    @PostMapping("/{id}/check-out")
    public StayDTO checkOut(@PathVariable Long id, @RequestParam Long userId) {
        return stayService.checkOut(id, userId);
    }

    @PostMapping("/checkout-by-reservation/{reservationId}")
    public StayDTO checkOutByReservationId(@PathVariable Long reservationId, @RequestParam Long userId) {
        return stayService.checkOutByReservationId(reservationId, userId);
    }

    @Data
    public static class CheckInRequest {
        private Long reservationId;
        private Long roomId;
        private Long userId;
    }

    @Data
    public static class WalkInRequest {
        private Long guestId;
        private Long roomId;
        private Integer adults;
        private Integer children;
        private java.time.LocalDateTime dateOut;
        private Long userId;
    }
}
