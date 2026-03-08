package com.hotel_erp.hotel_erp.modules.inventory;

import lombok.Data;

@Data
public class SupplierDTO {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String notes;
    private String contactPerson;
    private SupplierCategory category;
    private String paymentTerms;
    private boolean isActive;
}
