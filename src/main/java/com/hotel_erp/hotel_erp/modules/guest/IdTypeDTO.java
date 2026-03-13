package com.hotel_erp.hotel_erp.modules.guest;

import lombok.Data;

@Data
public class IdTypeDTO {
    private Long id;
    private String name;
    private String description;
    private boolean active;
}
