package com.hotel_erp.hotel_erp.modules.guest;

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

    public GuestServiceImpl(GuestRepository repository, 
                            GuestMapper mapper) {
        super(repository);
        this.mapper = mapper;
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
    public GuestDTO saveGuest(GuestDTO dto) {
        GuestEntity entity = mapper.toEntity(dto);
        entity = repository.save(entity);
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
        repository.findById(id).ifPresent(guest -> {
            guest.setVoided(true);
            repository.save(guest);
        });
    }
}
