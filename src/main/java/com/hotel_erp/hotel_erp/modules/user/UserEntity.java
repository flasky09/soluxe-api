package com.hotel_erp.hotel_erp.modules.user;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import com.hotel_erp.hotel_erp.modules.employee.DepartmentEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
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
    
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    @JsonProperty("isActive")
    @Builder.Default
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @Enumerated(EnumType.STRING)
    private Role role;
}
