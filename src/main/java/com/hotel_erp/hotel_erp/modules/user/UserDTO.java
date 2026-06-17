package com.hotel_erp.hotel_erp.modules.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Full name is required")
    private String fullName;
    
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @JsonProperty("isActive")
    private boolean active;
    
    private Long departmentId;
    
    @NotBlank(message = "Role is required")
    private String role;
    
    private String password;
    
    private Long createdBy;
    private Long modifiedBy;
    private String createdByName;
    private String modifiedByName;
}
