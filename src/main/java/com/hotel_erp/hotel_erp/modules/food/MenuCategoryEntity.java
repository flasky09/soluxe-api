package com.hotel_erp.hotel_erp.modules.food;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "menu_categories")
@EqualsAndHashCode(callSuper = true)
public class MenuCategoryEntity extends BaseEntity {
    @Column(unique = true)
    private String name;
    private String nameZh;
    private String description;
    private Integer sortOrder;
    
    @JsonProperty("isActive")
    private boolean isActive = true;
}

