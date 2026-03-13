package com.hotel_erp.hotel_erp.modules.stay;

import lombok.Data;

@Data
public class StayAdditionalGuestDTO {
    private Long id;
    private Long stayId;
    private String fullName;
    private String idType;
    private String idNumber;
}
