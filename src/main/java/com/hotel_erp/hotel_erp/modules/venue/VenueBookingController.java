package com.hotel_erp.hotel_erp.modules.venue;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venue-bookings")
@RequiredArgsConstructor
public class VenueBookingController {

    private final VenueBookingService venueBookingService;
    private final VenueBookingMapper venueBookingMapper;

    @GetMapping
    public List<VenueBookingDTO> getAllBookings() {
        return venueBookingService.findAll().stream()
                .map(venueBookingMapper::toDto)
                .toList();
    }

    @PostMapping
    public VenueBookingDTO createBooking(@RequestBody VenueBookingDTO bookingDto) {
        VenueBookingEntity entity = venueBookingMapper.toEntity(bookingDto);
        return venueBookingMapper.toDto(venueBookingService.save(entity));
    }

    @GetMapping("/{id}")
    public VenueBookingDTO getBookingById(@PathVariable Long id) {
        return venueBookingService.findById(id)
                .map(venueBookingMapper::toDto)
                .orElseThrow(() -> new RuntimeException("VenueBooking not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
        venueBookingService.deleteById(id);
    }
}
