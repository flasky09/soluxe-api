package com.hotel_erp.hotel_erp.modules.user;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import com.hotel_erp.hotel_erp.modules.employee.DepartmentEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    private String fullName;
    private String phoneNumber;
    
    @Column(unique = true)
    private String email;

    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @Enumerated(EnumType.STRING)
    private Role role;
}
