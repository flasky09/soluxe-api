package com.hotel_erp.hotel_erp.modules.guest;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.hotel_erp.hotel_erp.security.UserDetailsImpl;

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
    public GuestDTO createGuest(@Valid @RequestBody GuestDTO dto,
                                @AuthenticationPrincipal UserDetailsImpl principal) {
        Long userId = principal != null ? principal.getId() : null;
        return guestService.saveGuest(dto, userId);
    }

    @PutMapping("/{id}")
    public GuestDTO updateGuest(@PathVariable Long id, @Valid @RequestBody GuestDTO dto,
                                @AuthenticationPrincipal UserDetailsImpl principal) {
        Long userId = principal != null ? principal.getId() : null;
        dto.setId(id);
        return guestService.saveGuest(dto, userId);
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

    @PostMapping("/{id}/void")
    public void voidGuest(@PathVariable Long id) {
        guestService.voidGuest(id);
    }
}
