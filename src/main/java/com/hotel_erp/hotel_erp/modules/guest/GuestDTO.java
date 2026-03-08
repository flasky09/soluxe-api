package com.hotel_erp.hotel_erp.modules.guest;

import lombok.Data;
import java.time.LocalDate;

@Data
public class GuestDTO {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String nationality;
    private String address;
    private LocalDate dateOfBirth;
    private String gender;
    private String idType;
    private String idNumber;
    private String preferences;
    private String vehicleRegistration;
    private String emergencyContactName;
    private String emergencyContactPhone;
}
