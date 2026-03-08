package com.hotel_erp.hotel_erp.modules.food;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "menu_categories")
@EqualsAndHashCode(callSuper = true)
public class MenuCategoryEntity extends BaseEntity {
    private String name;
    private String nameZh;
    private String description;
    private Integer sortOrder;
    private boolean isActive = true;
}

