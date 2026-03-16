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

    @GetMapping("/active")
    public java.util.List<StayDTO> getActiveStays() {
        return stayService.findActiveStays();
    }

    @GetMapping("/guest/{guestId}")
    public java.util.List<StayDTO> getGuestStays(@PathVariable Long guestId) {
        return stayService.findByGuestId(guestId);
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
    public StayDTO checkOut(@PathVariable Long id, @RequestParam Long userId, @RequestParam(required = false, defaultValue = "false") boolean approveAdjustment) {
        // Since the interface doesn't have the boolean flag yet, we'll cast or call the service method directly if it's public
        // In this case, I'll update the interface too for consistency
        return ((StayServiceImpl)stayService).checkOut(id, userId, approveAdjustment);
    }

    @PostMapping("/{id}/extend")
    public StayDTO extendStay(@PathVariable Long id, @RequestParam java.time.LocalDateTime newDateOut, @RequestParam Long userId) {
        return stayService.extendStay(id, newDateOut, userId);
    }

    @PostMapping("/reservations/{reservationId}/no-show")
    public void markNoShow(@PathVariable Long reservationId, @RequestParam Long userId) {
        stayService.markNoShow(reservationId, userId);
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
