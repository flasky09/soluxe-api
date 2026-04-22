package com.hotel_erp.hotel_erp.modules.guest;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "guests")
@EqualsAndHashCode(callSuper = true)
public class GuestEntity extends BaseEntity {
    private String fullName;

    @Column(unique = true)
    private String phone;

    private String email;
    private String nationality;
    private String address;
    private String companyName;
    private LocalDate dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Enumerated(EnumType.STRING)
    private IdType idType;
    @Column(unique = true)
    private String idNumber;

    private String preferences;
    private String vehicleRegistration;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String imageUrl;
    @Builder.Default
    private boolean voided = false;
}
