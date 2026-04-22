package com.hotel_erp.hotel_erp.modules.food;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "menu_items")
@EqualsAndHashCode(callSuper = true)
public class MenuItemEntity extends BaseEntity {
    @Column(unique = true)
    private String name;
    private String nameZh;
    private String description;
    private String descriptionZh;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private SpiceLevel spiceLevel;

    private String allergens;
    private boolean signature;
    private Integer prepTimeMins;
    private Integer sortOrder;
    
    @ManyToOne
    private MenuCategoryEntity category;
    
    @Builder.Default
    private boolean available = true;
}
