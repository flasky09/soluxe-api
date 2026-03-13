package com.hotel_erp.hotel_erp.modules.expense;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "expense_types")
public class ExpenseTypeEntity extends BaseEntity {
    private String name;
    private String description;
}
