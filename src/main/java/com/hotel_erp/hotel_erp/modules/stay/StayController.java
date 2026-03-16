package com.hotel_erp.hotel_erp.modules.stay;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/stays")
@RequiredArgsConstructor
public class StayController {

    private final StayService stayService;
    private final StayMapper stayMapper;

    @GetMapping
    public List<StayDTO> getAllStays() {
        return stayService.findAll().stream()
                .map(stayMapper::toDto)
                .toList();
    }

    @GetMapping("/active")
    public List<StayDTO> getActiveStays() {
        return stayService.findActiveStays();
    }

    @GetMapping("/guest/{guestId}")
    public List<StayDTO> getGuestStays(@PathVariable("guestId") Long guestId) {
        return stayService.findByGuestId(guestId);
    }

    @PostMapping("/check-in")
    public StayDTO checkIn(@RequestBody CheckInRequest request) {
        return stayService.checkIn(request.getReservationId(), request.getRoomId(), request.getUserId());
    }

    @PostMapping("/direct")
    public StayDTO directCheckIn(@RequestBody DirectCheckInRequest request) {
        return stayService.directCheckIn(request.getGuestId(), request.getRoomId(), request.getAdults(), request.getChildren(), request.getDateOut(), request.getUserId());
    }

    @PostMapping("/{id}/check-out")
    public StayDTO checkOut(@PathVariable("id") Long id, @RequestParam("userId") Long userId, @RequestParam(name = "approveAdjustment", required = false, defaultValue = "false") boolean approveAdjustment) {
        // Since the interface doesn't have the boolean flag yet, we'll cast or call the service method directly if it's public
        // In this case, I'll update the interface too for consistency
        return ((StayServiceImpl)stayService).checkOut(id, userId, approveAdjustment);
    }

    @PostMapping("/{id}/extend")
    public StayDTO extendStay(@PathVariable("id") Long id, @RequestParam("newDateOut") LocalDateTime newDateOut, @RequestParam("userId") Long userId) {
        return stayService.extendStay(id, newDateOut, userId);
    }

    @PostMapping("/reservations/{reservationId}/no-show")
    public void markNoShow(@PathVariable("reservationId") Long reservationId, @RequestParam("userId") Long userId) {
        stayService.markNoShow(reservationId, userId);
    }

    @PostMapping("/{id}/void")
    public void voidStay(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
        stayService.voidStay(id, userId);
    }

    @Data
    public static class CheckInRequest {
        private Long reservationId;
        private Long roomId;
        private Long userId;
    }

    @Data
    public static class DirectCheckInRequest {
        private Long guestId;
        private Long roomId;
        private Integer adults;
        private Integer children;
        private java.time.LocalDate dateOut;
        private Long userId;
    }
}
