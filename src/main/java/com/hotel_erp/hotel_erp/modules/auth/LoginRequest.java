package com.hotel_erp.hotel_erp.modules.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
