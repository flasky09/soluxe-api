package com.hotel_erp.hotel_erp.modules.guest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Data
public class GuestDTO {
    private Long id;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @Email(message = "Invalid email format")
    private String email;
    
    private String nationality;
    private String address;
    private String companyName;
    private LocalDate dateOfBirth;
    private String gender;
    private String idType;

    @NotBlank(message = "ID number is required")
    private String idNumber;
    
    private String preferences;
    private String vehicleRegistration;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String imageUrl;
    private boolean voided;
    private Long createdBy;
    private Long modifiedBy;
    private String createdByName;
    private String modifiedByName;
}
