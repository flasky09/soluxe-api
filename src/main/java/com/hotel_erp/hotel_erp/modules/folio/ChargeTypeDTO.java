package com.hotel_erp.hotel_erp.modules.folio;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@lombok.Getter
@lombok.Setter
public class ChargeTypeDTO {
    private Long id;
    @NotBlank(message = "Charge type name is required")
    private String name;
    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
