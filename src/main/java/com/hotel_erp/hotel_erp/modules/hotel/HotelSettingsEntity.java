package com.hotel_erp.hotel_erp.modules.hotel;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "hotel_settings")
public class HotelSettingsEntity extends BaseEntity {
    private boolean allowWalkIn;
    private boolean allowComplimentary;
}
