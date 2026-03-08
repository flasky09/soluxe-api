package com.hotel_erp.hotel_erp.modules.hotel;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final HotelMapper hotelMapper;

    @GetMapping
    public List<HotelDTO> getAllHotels() {
        return hotelService.findAll().stream()
                .map(hotelMapper::toDto)
                .toList();
    }

    @PostMapping
    public HotelDTO createHotel(@RequestBody HotelDTO hotelDto) {
        HotelEntity entity = hotelMapper.toEntity(hotelDto);
        return hotelMapper.toDto(hotelService.save(entity));
    }

    @GetMapping("/{hotelIdentifier}")
    public HotelDTO getHotelById(@PathVariable Long hotelIdentifier) {
        return hotelService.findById(hotelIdentifier)
                .map(hotelMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
    }
}
