package com.hotel_erp.hotel_erp.modules.hotel;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hotels")
@EqualsAndHashCode(callSuper = true)
public class HotelEntity extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;
    
    private String address;
    private String phone;
    private String email;
    private String website;
    private String logoUrl;
}
