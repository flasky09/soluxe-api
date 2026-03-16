package com.hotel_erp.hotel_erp.modules.expense;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseTypeService {
    private final ExpenseTypeRepository expenseTypeRepository;

    public List<ExpenseTypeDTO> getAllExpenseTypes() {
        return expenseTypeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ExpenseTypeDTO getExpenseTypeById(Long id) {
        ExpenseTypeEntity entity = expenseTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense Type not found"));
        return mapToDTO(entity);
    }

    @Transactional
    public ExpenseTypeDTO createExpenseType(ExpenseTypeDTO dto) {
        ExpenseTypeEntity entity = new ExpenseTypeEntity();
        mapToEntity(dto, entity);
        return mapToDTO(expenseTypeRepository.save(entity));
    }

    @Transactional
    public ExpenseTypeDTO updateExpenseType(Long id, ExpenseTypeDTO dto) {
        ExpenseTypeEntity entity = expenseTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense Type not found"));
        mapToEntity(dto, entity);
        return mapToDTO(expenseTypeRepository.save(entity));
    }

    @Transactional
    public void deleteExpenseType(Long id) {
        expenseTypeRepository.deleteById(id);
    }

    private ExpenseTypeDTO mapToDTO(ExpenseTypeEntity entity) {
        ExpenseTypeDTO dto = new ExpenseTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    private void mapToEntity(ExpenseTypeDTO dto, ExpenseTypeEntity entity) {
        entity.setName(dto.getName());
    }
}
