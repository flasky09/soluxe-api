package com.hotel_erp.hotel_erp.modules.room;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "room_price_plans")
@EqualsAndHashCode(callSuper = true)
public class RoomPricePlanEntity extends BaseEntity {
    @Column(unique = true)
    private String name;
    private String nameZh;
    private String description;
    private String descriptionZh;
    private boolean isActive = true;
}
