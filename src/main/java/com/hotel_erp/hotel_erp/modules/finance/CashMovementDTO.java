package com.hotel_erp.hotel_erp.modules.finance;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class CashMovementDTO {
    private Long id;
    private CashMovementEntity.CashMovementType type;
    private BigDecimal amount;
    private LocalDate movementDate;
    private String description;
}
