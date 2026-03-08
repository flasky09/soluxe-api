package com.hotel_erp.hotel_erp.modules.food;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DiningOrderDTO {
    private Long id;
    private Long sessionId;
    private Long menuItemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountPct;
    private BigDecimal taxPct;
    private BigDecimal totalAmount;
    private String notes;
    private OrderStatus status;
    private LocalDateTime orderedAt;
    private Integer roundNo;
    private LocalDateTime servedAt;

}
