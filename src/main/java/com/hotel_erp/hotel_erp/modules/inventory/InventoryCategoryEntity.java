package com.hotel_erp.hotel_erp.modules.inventory;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "inventory_categories")
@EqualsAndHashCode(callSuper = true)
public class InventoryCategoryEntity extends BaseEntity {
    private String name;
    private String description;
}
