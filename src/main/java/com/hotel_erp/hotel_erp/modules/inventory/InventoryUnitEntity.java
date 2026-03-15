package com.hotel_erp.hotel_erp.modules.inventory;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "inventory_units")
@EqualsAndHashCode(callSuper = true)
public class InventoryUnitEntity extends BaseEntity {
    @Column(unique = true)
    private String name;
    private String description;
    private boolean active = true;
}
