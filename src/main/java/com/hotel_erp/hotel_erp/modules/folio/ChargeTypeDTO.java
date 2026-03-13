package com.hotel_erp.hotel_erp.modules.folio;

import lombok.Data;

@Data
public class ChargeTypeDTO {
    private Long id;
    private String name;
    private String description;
    private boolean active;
}
