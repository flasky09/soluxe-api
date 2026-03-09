package com.hotel_erp.hotel_erp.modules.food;

import com.hotel_erp.hotel_erp.modules.inventory.InventoryItemEntity;
import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "recipe_items")
@EqualsAndHashCode(callSuper = true)
public class RecipeItemEntity extends BaseEntity {
    
    @ManyToOne
    private MenuItemEntity menuItem;
    
    @ManyToOne
    private InventoryItemEntity ingredient;
    
    private BigDecimal quantity;
}
