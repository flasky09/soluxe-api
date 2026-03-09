package com.hotel_erp.hotel_erp.modules.guest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;
    private final GuestMapper guestMapper;

    @PostMapping
    public GuestDTO createGuest(@RequestBody GuestDTO guestDTO) {
        GuestEntity guestEntity = guestMapper.toEntity(guestDTO);
        GuestEntity savedGuest = guestService.save(guestEntity);
        return guestMapper.toDto(savedGuest);
    }

    @PutMapping("/{id}")
    public GuestDTO updateGuest(@PathVariable Long id, @RequestBody GuestDTO guestDTO) {
        GuestEntity guestEntity = guestMapper.toEntity(guestDTO);
        guestEntity.setId(id);
        GuestEntity savedGuest = guestService.save(guestEntity);
        return guestMapper.toDto(savedGuest);
    }

    @GetMapping("/{guestIdentifier}")
    public GuestDTO getGuestById(@PathVariable Long guestIdentifier) {
        GuestEntity guestEntity = guestService.findById(guestIdentifier)
            .orElseThrow(() -> new RuntimeException("Guest not found"));
        return guestMapper.toDto(guestEntity);
    }

    @GetMapping
    public List<GuestDTO> getAllGuests() {
        return guestService.findAll().stream()
            .map(guestMapper::toDto)
            .toList();
    }
}
