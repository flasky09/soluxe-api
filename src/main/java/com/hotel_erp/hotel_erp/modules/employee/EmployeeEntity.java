package com.hotel_erp.hotel_erp.modules.employee;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.hotel_erp.hotel_erp.modules.guest.IdType;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
@EqualsAndHashCode(callSuper = true)
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

    private String kraPin;
    
    @Builder.Default
    private boolean active = true;
}
