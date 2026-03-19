package com.hotel_erp.hotel_erp.modules.food;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class MenuItemDTO {
    private Long id;
    @NotBlank(message = "Item name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private BigDecimal price;
    
    @NotNull(message = "Category is required")
    private Long categoryId;
    
    private boolean available;
    
    @Min(value = 0, message = "Prep time cannot be negative")
    private Integer prepTimeMins;
}
