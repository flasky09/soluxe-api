package com.hotel_erp.hotel_erp.modules.guest;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "identity_types")
@EqualsAndHashCode(callSuper = true)
public class IdentityTypeEntity extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;
    
    private String description;
    
    private boolean active = true;
}
