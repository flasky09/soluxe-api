package com.hotel_erp.hotel_erp.modules.expense;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseTypeRepository expenseTypeRepository;

    public List<ExpenseDTO> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ExpenseDTO getExpenseById(Long id) {
        ExpenseEntity entity = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        return mapToDTO(entity);
    }

    @Transactional
    public ExpenseDTO createExpense(ExpenseDTO dto) {
        return createExpense(dto, null);
    }

    @Transactional
    public ExpenseDTO createExpense(ExpenseDTO dto, Long userId) {
        ExpenseEntity entity = new ExpenseEntity();
        mapToEntity(dto, entity);
        if (userId != null) {
            entity.setCreatedBy(userId);
        }
        return mapToDTO(expenseRepository.save(entity));
    }

    @Transactional
    public ExpenseDTO updateExpense(Long id, ExpenseDTO dto) {
        return updateExpense(id, dto, null);
    }

    @Transactional
    public ExpenseDTO updateExpense(Long id, ExpenseDTO dto, Long userId) {
        ExpenseEntity entity = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        mapToEntity(dto, entity);
        if (userId != null) {
            entity.setModifiedBy(userId);
        }
        return mapToDTO(expenseRepository.save(entity));
    }

    @Transactional
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    private ExpenseDTO mapToDTO(ExpenseEntity entity) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setAmount(entity.getAmount());
        dto.setExpenseDate(entity.getExpenseDate());
        
        if (entity.getExpenseType() != null) {
            ExpenseTypeDTO typeDto = new ExpenseTypeDTO();
            typeDto.setId(entity.getExpenseType().getId());
            typeDto.setName(entity.getExpenseType().getName());
            dto.setExpenseType(typeDto);
        }
        
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setReferenceNumber(entity.getReferenceNumber());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifiedBy(entity.getModifiedBy());
        return dto;
    }

    private void mapToEntity(ExpenseDTO dto, ExpenseEntity entity) {
        entity.setDescription(dto.getDescription());
        entity.setAmount(dto.getAmount());
        entity.setExpenseDate(dto.getExpenseDate());
        
        if (dto.getExpenseType() != null && dto.getExpenseType().getId() != null) {
            ExpenseTypeEntity typeEntity = expenseTypeRepository.findById(dto.getExpenseType().getId())
                    .orElseThrow(() -> new RuntimeException("Expense Type not found"));
            entity.setExpenseType(typeEntity);
        }
        
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setReferenceNumber(dto.getReferenceNumber());
    }
}
