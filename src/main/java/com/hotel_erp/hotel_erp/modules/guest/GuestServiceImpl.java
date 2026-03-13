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
    
    private final IdTypeRepository idTypeRepository;
    private final GuestMapper mapper;

    public GuestServiceImpl(GuestRepository repository, 
                            IdTypeRepository idTypeRepository, 
                            GuestMapper mapper) {
        super(repository);
        this.idTypeRepository = idTypeRepository;
        this.mapper = mapper;
    }

    @Override
    public List<GuestDTO> findAllGuests() {
        return repository.findAll().stream()
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
        
        if (dto.getIdTypeId() != null) {
            idTypeRepository.findById(dto.getIdTypeId())
                    .ifPresent(entity::setIdType);
        }

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }
}
