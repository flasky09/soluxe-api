package com.hotel_erp.hotel_erp.modules.hotel;

import lombok.Data;

@Data
public class HotelDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
}
