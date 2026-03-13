package com.hotel_erp.hotel_erp.modules.venue;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PutMapping("/{id}")
    public ResponseEntity<VenueBookingDTO> updateBooking(@PathVariable Long id, @RequestBody VenueBookingDTO bookingDto) {
        return venueBookingService.findById(id).map(existing -> {
            VenueBookingEntity updated = venueBookingMapper.toEntity(bookingDto);
            updated.setId(existing.getId());
            updated.setCreatedAt(existing.getCreatedAt());
            return ResponseEntity.ok(venueBookingMapper.toDto(venueBookingService.save(updated)));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<VenueBookingDTO> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return venueBookingService.findById(id).map(existing -> {
            existing.setStatus(BookingStatus.valueOf(body.get("status")));
            return ResponseEntity.ok(venueBookingMapper.toDto(venueBookingService.save(existing)));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
        venueBookingService.deleteById(id);
    }
}
