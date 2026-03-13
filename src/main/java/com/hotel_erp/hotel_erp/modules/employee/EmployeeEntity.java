package com.hotel_erp.hotel_erp.modules.employee;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;


import com.hotel_erp.hotel_erp.modules.guest.IdType;


@Getter
@Setter
@Entity
@Table(name = "employees")
public class EmployeeEntity extends BaseEntity {
    private String fullName;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    private String email;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    private String designation;
    private String languagesSpoken; // Standard list of international languages
    private BigDecimal basicSalary;
    private LocalDate dateOfJoining;
    
    private String nationality;
    
    @Enumerated(EnumType.STRING)
    private IdType idType;

    @Column(unique = true, nullable = false)
    private String idNumber;
    
    private boolean isActive = true;
}

