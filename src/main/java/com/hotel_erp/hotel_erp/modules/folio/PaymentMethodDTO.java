package com.hotel_erp.hotel_erp.modules.folio;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDTO {
    private Long id;
    @NotBlank(message = "Payment method name is required")
    private String name;
    private String description;
    private boolean active;
}
