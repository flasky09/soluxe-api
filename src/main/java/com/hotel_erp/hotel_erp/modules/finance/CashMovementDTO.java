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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CashMovementEntity.CashMovementType getType() {
        return type;
    }

    public void setType(CashMovementEntity.CashMovementType type) {
        this.type = type;
    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
    }

    public java.time.LocalDate getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(java.time.LocalDate movementDate) {
        this.movementDate = movementDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
