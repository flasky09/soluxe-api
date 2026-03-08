package com.hotel_erp.hotel_erp.modules.inventory;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Entity
@Table(name = "suppliers")
@EqualsAndHashCode(callSuper = true)
public class SupplierEntity extends BaseEntity {
    private String name;
    private String contactPerson;

    @Enumerated(EnumType.STRING)
    private SupplierCategory category;

    private String phone;
    private String email;
    private String address;
    private String paymentTerms;
    private String notes;
    private boolean isActive = true;
}

