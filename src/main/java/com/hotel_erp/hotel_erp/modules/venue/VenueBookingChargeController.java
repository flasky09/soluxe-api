package com.hotel_erp.hotel_erp.modules.venue;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venue-booking-charges")
@RequiredArgsConstructor
public class VenueBookingChargeController {

    private final VenueBookingChargeService venueBookingChargeService;
    private final VenueBookingChargeMapper venueBookingChargeMapper;

    @GetMapping("/booking/{bookingId}")
    public List<VenueBookingChargeDTO> getChargesByBooking(@PathVariable Long bookingId) {
        return venueBookingChargeService.findByBookingId(bookingId).stream()
                .map(venueBookingChargeMapper::toDto)
                .toList();
    }

    @PostMapping
    public VenueBookingChargeDTO createCharge(@RequestBody VenueBookingChargeDTO chargeDto) {
        VenueBookingChargeEntity entity = venueBookingChargeMapper.toEntity(chargeDto);
        return venueBookingChargeMapper.toDto(venueBookingChargeService.save(entity));
    }

    @GetMapping("/{id}")
    public VenueBookingChargeDTO getChargeById(@PathVariable Long id) {
        return venueBookingChargeService.findById(id)
                .map(venueBookingChargeMapper::toDto)
                .orElseThrow(() -> new RuntimeException("VenueBookingCharge not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteCharge(@PathVariable Long id) {
        venueBookingChargeService.deleteById(id);
    }
}
