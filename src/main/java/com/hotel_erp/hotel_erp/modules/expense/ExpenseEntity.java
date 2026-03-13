package com.hotel_erp.hotel_erp.modules.expense;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "expenses")
public class ExpenseEntity extends BaseEntity {
    private String description;
    
    @Column(precision = 19, scale = 2)
    private BigDecimal amount;
    
    private LocalDate expenseDate;
    
    @ManyToOne
    @JoinColumn(name = "expense_type_id")
    private ExpenseTypeEntity expenseType;
    
    private String paymentMethod;
    private String referenceNumber;
}
