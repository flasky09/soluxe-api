package com.hotel_erp.hotel_erp.modules.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
    List<ExpenseEntity> findAllByExpenseDateBetween(java.time.LocalDate startDate, java.time.LocalDate endDate);
}
