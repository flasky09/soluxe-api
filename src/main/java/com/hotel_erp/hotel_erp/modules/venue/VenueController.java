package com.hotel_erp.hotel_erp.modules.venue;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;
    private final VenueMapper venueMapper;

    @GetMapping
    public List<VenueDTO> getAllVenues() {
        return venueService.findAll().stream()
                .map(venueMapper::toDto)
                .toList();
    }

    @PostMapping
    public VenueDTO createVenue(@RequestBody VenueDTO venueDto) {
        VenueEntity entity = venueMapper.toEntity(venueDto);
        return venueMapper.toDto(venueService.save(entity));
    }

    @PutMapping("/{id}")
    public VenueDTO updateVenue(@PathVariable Long id, @RequestBody VenueDTO venueDto) {
        VenueEntity entity = venueMapper.toEntity(venueDto);
        entity.setId(id);
        return venueMapper.toDto(venueService.save(entity));
    }

    @GetMapping("/{id}")
    public VenueDTO getVenueById(@PathVariable Long id) {
        return venueService.findById(id)
                .map(venueMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Venue not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteVenue(@PathVariable Long id) {
        venueService.deleteById(id);
    }
}
