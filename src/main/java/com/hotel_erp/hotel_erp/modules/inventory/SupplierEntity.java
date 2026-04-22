package com.hotel_erp.hotel_erp.modules.inventory;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
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
    @Builder.Default
    private boolean isActive = true;
}
