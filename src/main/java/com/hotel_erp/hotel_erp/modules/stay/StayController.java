package com.hotel_erp.hotel_erp.modules.stay;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stays")
@RequiredArgsConstructor
public class StayController {

    private final StayService stayService;

    @PostMapping("/check-in")
    public StayDTO checkIn(@RequestBody CheckInRequest request) {
        return stayService.checkIn(request.getReservationId(), request.getRoomId(), request.getUserId());
    }

    @PostMapping("/{id}/check-out")
    public StayDTO checkOut(@PathVariable Long id, @RequestParam Long userId) {
        return stayService.checkOut(id, userId);
    }

    @Data
    public static class CheckInRequest {
        private Long reservationId;
        private Long roomId;
        private Long userId;
    }
}
