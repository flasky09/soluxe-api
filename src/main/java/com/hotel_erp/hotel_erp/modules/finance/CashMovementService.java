package com.hotel_erp.hotel_erp.modules.finance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CashMovementService {
    private final CashMovementRepository repository;

    public List<CashMovementDTO> getAllMovements() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CashMovementDTO createMovement(CashMovementDTO dto) {
        CashMovementEntity entity = new CashMovementEntity();
        mapToEntity(dto, entity);
        return mapToDTO(repository.save(entity));
    }

    @Transactional
    public void deleteMovement(Long id) {
        repository.deleteById(id);
    }

    private CashMovementDTO mapToDTO(CashMovementEntity entity) {
        CashMovementDTO dto = new CashMovementDTO();
        dto.setId(entity.getId());
        dto.setType(entity.getType());
        dto.setAmount(entity.getAmount());
        dto.setMovementDate(entity.getMovementDate());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    private void mapToEntity(CashMovementDTO dto, CashMovementEntity entity) {
        entity.setType(dto.getType());
        entity.setAmount(dto.getAmount());
        entity.setMovementDate(dto.getMovementDate());
        entity.setDescription(dto.getDescription());
    }
}
