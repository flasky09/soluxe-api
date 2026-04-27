package com.hotel_erp.hotel_erp.modules.expense;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseDTO {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate expenseDate;
    private ExpenseTypeDTO expenseType;
    private String paymentMethod;
    private String currencyCode;
    private BigDecimal exchangeRate;
    private String referenceNumber;
    private Long createdBy;
    private Long modifiedBy;
}
