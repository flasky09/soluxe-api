package com.hotel_erp.hotel_erp.modules.expense;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expense_types")
@EqualsAndHashCode(callSuper = true)
public class ExpenseTypeEntity extends BaseEntity {
    @Column(unique = true)
    private String name;
    
    @Column(nullable = false)
    @Builder.Default
    private boolean isAsset = false;
}
