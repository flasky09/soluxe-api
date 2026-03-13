package com.hotel_erp.hotel_erp.modules.guest;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "guests")
public class GuestEntity extends BaseEntity {
    private String fullName;

    @Column(unique = true)
    private String phone;

    @Column(unique = true)
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
}
