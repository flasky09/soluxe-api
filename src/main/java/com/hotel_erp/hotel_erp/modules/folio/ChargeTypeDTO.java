package com.hotel_erp.hotel_erp.modules.folio;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChargeTypeDTO {
    private Long id;
    @NotBlank(message = "Charge type name is required")
    private String name;
    private boolean active;
}
