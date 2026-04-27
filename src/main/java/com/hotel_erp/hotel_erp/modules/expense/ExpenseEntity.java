package com.hotel_erp.hotel_erp.modules.expense;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expenses")
@EqualsAndHashCode(callSuper = true)
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
    
    @Column(length = 10)
    private String currencyCode;
    
    @Column(precision = 19, scale = 4)
    private BigDecimal exchangeRate;
}
