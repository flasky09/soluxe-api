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
    private final com.hotel_erp.hotel_erp.modules.activity.ActivityLogService activityLogService;

    public VenueBookingEntity save(VenueBookingEntity entity, Long userId) {
        if (userId != null) {
            if (entity.getId() == null) {
                entity.setCreatedBy(userId);
            } else {
                entity.setModifiedBy(userId);
            }
        }
        VenueBookingEntity saved = repository.save(entity);
        String action = entity.getId() == null ? "CREATE_VENUE_BOOKING" : "UPDATE_VENUE_BOOKING";
        activityLogService.logActivity(userId, action, "Saved venue booking for Guest ID: " + saved.getGuestId());
        return saved;
    }

    public List<VenueBookingEntity> findAll() {
        return repository.findAll();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public VenueBookingDTO create(VenueBookingDTO dto, Long userId) {
        VenueBookingEntity entity = mapper.toEntity(dto);
        return mapper.toDto(save(entity, userId));
    }

    public Optional<VenueBookingEntity> findById(Long id) {
        return repository.findById(id);
    }
}
