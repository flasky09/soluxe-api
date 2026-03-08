package com.hotel_erp.hotel_erp.modules.venue;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VenueBookingService {
    private final VenueBookingRepository repository;
    private final VenueBookingMapper mapper;

    public VenueBookingEntity save(VenueBookingEntity entity) {
        return repository.save(entity);
    }

    public List<VenueBookingEntity> findAll() {
        return repository.findAll();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public VenueBookingDTO create(VenueBookingDTO dto) {
        VenueBookingEntity entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public Optional<VenueBookingEntity> findById(Long id) {
        return repository.findById(id);
    }
}
