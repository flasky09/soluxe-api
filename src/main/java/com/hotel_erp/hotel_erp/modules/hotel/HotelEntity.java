package com.hotel_erp.hotel_erp.modules.hotel;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "hotels")
public class HotelEntity extends BaseEntity {
    private String name;
    private String address;
    private String phone;
    private String email;
}
