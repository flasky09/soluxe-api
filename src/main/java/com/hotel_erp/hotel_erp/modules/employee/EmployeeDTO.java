package com.hotel_erp.hotel_erp.modules.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeDTO {
    private Long id;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Designation is required")
    private String designation;

    private BigDecimal basicSalary;
    private LocalDate dateOfJoining;
    private String languagesSpoken;
    private Long departmentId;
    private String nationality;
    private String idType;

    @NotBlank(message = "ID number is required")
    private String idNumber;
}
