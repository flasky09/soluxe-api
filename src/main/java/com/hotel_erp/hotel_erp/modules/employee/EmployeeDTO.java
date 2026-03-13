package com.hotel_erp.hotel_erp.modules.employee;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeDTO {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String designation;
    private BigDecimal basicSalary;
    private LocalDate dateOfJoining;
    private String languagesSpoken;
    private Long departmentId;
    private String nationality;
    private Long idTypeId;
    private String idTypeName;
    private String idNumber;
}
