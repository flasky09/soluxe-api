package com.hotel_erp.hotel_erp.modules.guest;

import com.hotel_erp.hotel_erp.modules.stay.StayRepository;
import com.hotel_erp.hotel_erp.modules.stay.StayStatus;
import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GuestServiceImpl extends BaseServiceImpl<GuestEntity, Long, GuestRepository> implements GuestService {
    
    private final GuestMapper mapper;
    private final StayRepository stayRepository;
    private final com.hotel_erp.hotel_erp.modules.activity.ActivityLogService activityLogService;

    public GuestServiceImpl(GuestRepository repository,
                            GuestMapper mapper,
                            StayRepository stayRepository,
                            com.hotel_erp.hotel_erp.modules.activity.ActivityLogService activityLogService) {
        super(repository);
        this.mapper = mapper;
        this.stayRepository = stayRepository;
        this.activityLogService = activityLogService;
    }

    @Override
    public List<GuestDTO> findAllGuests() {
        return repository.findAllByOrderByIdDesc().stream()
                .filter(g -> !g.isVoided())
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GuestDTO> findGuestById(Long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    @Transactional
    public GuestDTO saveGuest(GuestDTO dto, Long userId) {
        GuestEntity entity = mapper.toEntity(dto);
        
        if (userId != null) {
            if (entity.getId() == null) {
                entity.setCreatedBy(userId);
            } else {
                entity.setModifiedBy(userId);
            }
        }
        
        entity = repository.save(entity);
        
        String action = dto.getId() == null ? "CREATE_GUEST" : "UPDATE_GUEST";
        activityLogService.logActivity(userId, action, "Saved guest profile: " + entity.getFullName());
        
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        voidGuest(id);
    }

    @Override
    @Transactional
    public void voidGuest(Long id) {
        // BUG 9 FIX: Prevent voiding a guest who has an active stay to avoid orphaned stay records.
        long activeStays = stayRepository.countByGuestIdAndStatusIn(
                id, java.util.List.of(StayStatus.ACTIVE, StayStatus.OVERSTAY, StayStatus.DUE_CHECKOUT));
        if (activeStays > 0) {
            throw new RuntimeException("Cannot void guest with an active stay. Please check out the guest first.");
        }
        repository.findById(id).ifPresent(guest -> {
            guest.setVoided(true);
            repository.save(guest);
        });
    }
}
