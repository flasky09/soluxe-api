package com.hotel_erp.hotel_erp.modules.stay;

import com.hotel_erp.hotel_erp.modules.guest.IdType;
import lombok.Data;

@Data
public class StayAdditionalGuestDTO {
    private Long id;
    private Long stayId;
    private String fullName;
    private IdType idType;
    private String idNumber;
}
