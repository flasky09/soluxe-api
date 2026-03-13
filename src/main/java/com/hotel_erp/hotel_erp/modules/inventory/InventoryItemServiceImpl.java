package com.hotel_erp.hotel_erp.modules.inventory;

import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryItemServiceImpl extends BaseServiceImpl<InventoryItemEntity, Long, InventoryItemRepository> implements InventoryItemService {
    
    private final InventoryUnitRepository unitRepository;
    private final InventoryItemMapper mapper;

    public InventoryItemServiceImpl(InventoryItemRepository repository, 
                                    InventoryUnitRepository unitRepository, 
                                    InventoryItemMapper mapper) {
        super(repository);
        this.unitRepository = unitRepository;
        this.mapper = mapper;
    }

    @Override
    public List<InventoryItemDTO> findAllItems() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<InventoryItemDTO> findItemById(Long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    @Transactional
    public InventoryItemDTO saveItem(InventoryItemDTO dto) {
        InventoryItemEntity entity = mapper.toEntity(dto);
        
        if (dto.getUnitId() != null) {
            unitRepository.findById(dto.getUnitId())
                    .ifPresent(entity::setUnit);
        }

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }
}

