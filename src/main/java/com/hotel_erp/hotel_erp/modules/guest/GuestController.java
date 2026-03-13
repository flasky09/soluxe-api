package com.hotel_erp.hotel_erp.modules.guest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    @GetMapping
    public List<GuestDTO> getAllGuests() {
        return guestService.findAllGuests();
    }

    @PostMapping
    public GuestDTO createGuest(@RequestBody GuestDTO dto) {
        return guestService.saveGuest(dto);
    }

    @PutMapping("/{id}")
    public GuestDTO updateGuest(@PathVariable Long id, @RequestBody GuestDTO dto) {
        dto.setId(id);
        return guestService.saveGuest(dto);
    }

    @GetMapping("/{id}")
    public GuestDTO getGuestById(@PathVariable Long id) {
        return guestService.findGuestById(id)
                .orElseThrow(() -> new RuntimeException("Guest not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteGuest(@PathVariable Long id) {
        guestService.deleteById(id);
    }
}
