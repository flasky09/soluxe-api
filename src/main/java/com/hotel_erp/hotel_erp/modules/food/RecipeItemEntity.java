package com.hotel_erp.hotel_erp.modules.food;

import com.hotel_erp.hotel_erp.modules.inventory.InventoryItemEntity;
import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
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
