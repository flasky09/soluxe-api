package com.hotel_erp.hotel_erp.modules.food;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "restaurant_tables")
@EqualsAndHashCode(callSuper = true)
public class RestaurantTableEntity extends BaseEntity {
    private String tableName;
    private int capacity;
    
    @Enumerated(EnumType.STRING)
    private TableLocation location;

    private boolean isVip;
    private String notes;

    @Enumerated(EnumType.STRING)
    private TableStatus status = TableStatus.AVAILABLE;
}

