package com.hotel_erp.hotel_erp.modules.finance;

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
@Table(name = "cash_movements")
@EqualsAndHashCode(callSuper = true)
public class CashMovementEntity extends BaseEntity {
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CashMovementType type;
    
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private LocalDate movementDate;
    
    private String description;
    
    public enum CashMovementType {
        DRAWING,
        SAVING,
        CAPITAL_INJECTION
    }
}
