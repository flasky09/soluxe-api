package com.hotel_erp.hotel_erp.modules.stay;

import com.hotel_erp.hotel_erp.security.UserDetailsImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/stays")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('RECEPTIONIST', 'HOTEL_ADMIN', 'MANAGER')")
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
        return stayService.directCheckIn(
                request.getGuestId(), request.getRoomId(),
                request.getAdults(), request.getChildren(),
                request.getDateOut(), request.getUserId());
    }

    @PostMapping("/{id}/check-out")
    public StayDTO checkOut(@PathVariable("id") Long id,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(name = "approveAdjustment", required = false, defaultValue = "false") boolean approveAdjustment,
            @AuthenticationPrincipal UserDetailsImpl principal) {
        Long resolvedUserId = userId != null ? userId : (principal != null ? principal.getId() : null);
        return stayService.checkOut(id, resolvedUserId, approveAdjustment);
    }

    @PostMapping("/{id}/extend")
    public StayDTO extendStay(
            @PathVariable("id") Long id,
            @RequestParam("newDateOut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newDateOut,
            @RequestParam(value = "userId", required = false) Long userId,
            @AuthenticationPrincipal UserDetailsImpl principal) {
        Long resolvedUserId = userId != null ? userId : (principal != null ? principal.getId() : null);
        return stayService.extendStay(id, newDateOut.atTime(12, 0), resolvedUserId);
    }

    @PostMapping("/reservations/{reservationId}/no-show")
    public void markNoShow(@PathVariable("reservationId") Long reservationId,
                           @RequestParam(value = "userId", required = false) Long userId,
                           @AuthenticationPrincipal UserDetailsImpl principal) {
        Long resolvedUserId = userId != null ? userId : (principal != null ? principal.getId() : null);
        stayService.markNoShow(reservationId, resolvedUserId);
    }

    @PostMapping("/{id}/void")
    public void voidStay(@PathVariable("id") Long id,
                         @RequestParam(value = "userId", required = false) Long userId,
                         @AuthenticationPrincipal UserDetailsImpl principal) {
        Long resolvedUserId = userId != null ? userId : (principal != null ? principal.getId() : null);
        stayService.voidStay(id, resolvedUserId);
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
