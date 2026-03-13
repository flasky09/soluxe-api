package com.hotel_erp.hotel_erp.modules.inventory;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "suppliers")
@EqualsAndHashCode(callSuper = true)
public class SupplierEntity extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;
    
    private String contactPerson;

    @Enumerated(EnumType.STRING)
    private SupplierCategory category;

    private String phone;

    @Column(unique = true, nullable = false)
    private String email;
    
    private String address;
    private String paymentTerms;
    private String notes;
    
    @JsonProperty("isActive")
    private boolean isActive = true;
}
