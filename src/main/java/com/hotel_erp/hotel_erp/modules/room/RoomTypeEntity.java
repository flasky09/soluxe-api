package com.hotel_erp.hotel_erp.modules.room;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "room_types")
public class RoomTypeEntity extends BaseEntity {
    @Column(unique = true)
    private String name;
    private BigDecimal defaultRate;
    private BigDecimal weekendRate;
    
    private Integer capacity;
    private String bedType;
    
    @Column(columnDefinition = "TEXT")
    private String amenities;
}
