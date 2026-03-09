package com.hotel_erp.hotel_erp.modules.user;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private String phoneNumber;
    private String email;
    private boolean active;
    private Long departmentId;
    private String role;
    private String password;
}
