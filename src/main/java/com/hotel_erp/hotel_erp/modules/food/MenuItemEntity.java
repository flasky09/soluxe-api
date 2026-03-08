package com.hotel_erp.hotel_erp.modules.food;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "menu_items")
@EqualsAndHashCode(callSuper = true)
public class MenuItemEntity extends BaseEntity {
    private String name;
    private String nameZh;
    private String description;
    private String descriptionZh;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private SpiceLevel spiceLevel;

    private String allergens;
    private boolean isSignature;
    private Integer prepTimeMins;
    private Integer sortOrder;
    
    @ManyToOne
    private MenuCategoryEntity category;
    
    private boolean available = true;
}

