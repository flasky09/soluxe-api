package com.hotel_erp.hotel_erp.modules.expense;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense-types")
@RequiredArgsConstructor
public class ExpenseControllerTypes {
    private final ExpenseTypeService expenseTypeService;

    @GetMapping
    public List<ExpenseTypeDTO> getAllExpenseTypes() {
        return expenseTypeService.getAllExpenseTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseTypeDTO> getExpenseTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(expenseTypeService.getExpenseTypeById(id));
    }

    @PostMapping
    public ExpenseTypeDTO createExpenseType(@RequestBody ExpenseTypeDTO dto) {
        return expenseTypeService.createExpenseType(dto);
    }

    @PutMapping("/{id}")
    public ExpenseTypeDTO updateExpenseType(@PathVariable Long id, @RequestBody ExpenseTypeDTO dto) {
        return expenseTypeService.updateExpenseType(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenseType(@PathVariable Long id) {
        expenseTypeService.deleteExpenseType(id);
        return ResponseEntity.noContent().build();
    }
}
