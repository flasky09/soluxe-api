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
        PettyCashEntity entity = PettyCashEntity.builder()
                .amount(dto.getAmount())
                .expenseDate(dto.getExpenseDate())
                .description(dto.getDescription())
                .issuedTo(dto.getIssuedTo())
                .category(dto.getCategory())
                .build();
        return convertToDTO(repository.save(entity));
    }

    @Transactional
    public void deleteEntry(Long id) {
        repository.deleteById(id);
    }

    private PettyCashDTO convertToDTO(PettyCashEntity entity) {
        return PettyCashDTO.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .expenseDate(entity.getExpenseDate())
                .description(entity.getDescription())
                .issuedTo(entity.getIssuedTo())
                .category(entity.getCategory())
                .build();
    }
}
