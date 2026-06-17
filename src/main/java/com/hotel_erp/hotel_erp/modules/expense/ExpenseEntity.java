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
    private java.math.BigDecimal exchangeRate;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
    }

    public java.time.LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(java.time.LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public ExpenseTypeEntity getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseTypeEntity expenseType) {
        this.expenseType = expenseType;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public java.math.BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(java.math.BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
