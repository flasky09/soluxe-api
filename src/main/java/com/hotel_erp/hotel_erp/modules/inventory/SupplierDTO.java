package com.hotel_erp.hotel_erp.modules.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupplierDTO {
    private Long id;

    @NotBlank(message = "Supplier name is required")
    private String name;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String address;
    private String notes;
    private String contactPerson;
    private SupplierCategory category;
    private String paymentTerms;
    
    @JsonProperty("isActive")
    private boolean isActive;
}
