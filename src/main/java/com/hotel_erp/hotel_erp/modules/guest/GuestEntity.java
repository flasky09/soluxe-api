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
    private String phone;
    private String email;
    private String nationality;
    private String address;
    private String companyName;
    private LocalDate dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_entity_id")
    private IdTypeEntity idType;
    private String idNumber;

    private String preferences;
    private String vehicleRegistration;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String imageUrl;
}
