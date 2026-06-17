package com.hotel_erp.hotel_erp.modules.finance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PettyCashService {
    private final PettyCashRepository repository;

    public List<PettyCashDTO> getAllEntries() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PettyCashDTO createEntry(PettyCashDTO dto) {
        PettyCashEntity entity = new PettyCashEntity();
        entity.setAmount(dto.getAmount());
        entity.setExpenseDate(dto.getExpenseDate());
        entity.setDescription(dto.getDescription());
        entity.setIssuedTo(dto.getIssuedTo());
        entity.setCategory(dto.getCategory());
        return convertToDTO(repository.save(entity));
    }

    @Transactional
    public void deleteEntry(Long id) {
        repository.deleteById(id);
    }

    private PettyCashDTO convertToDTO(PettyCashEntity entity) {
        PettyCashDTO dto = new PettyCashDTO();
        dto.setId(entity.getId());
        dto.setAmount(entity.getAmount());
        dto.setExpenseDate(entity.getExpenseDate());
        dto.setDescription(entity.getDescription());
        dto.setIssuedTo(entity.getIssuedTo());
        dto.setCategory(entity.getCategory());
        return dto;
    }
}
