package com.hotel_erp.hotel_erp.modules.food;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "menu_categories")
@EqualsAndHashCode(callSuper = true)
public class MenuCategoryEntity extends BaseEntity {
    @Column(unique = true)
    private String name;
    private String nameZh;
    private Integer sortOrder;
    
    @JsonProperty("isActive")
    @Builder.Default
    private boolean isActive = true;
}
