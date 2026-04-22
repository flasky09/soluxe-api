package com.hotel_erp.hotel_erp.modules.finance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PettyCashDTO {
    private Long id;
    private BigDecimal amount;
    private LocalDate expenseDate;
    private String description;
    private String issuedTo;
    private String category;
}
