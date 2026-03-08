package com.hotel_erp.hotel_erp.modules.food;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DiningSessionDTO {
    private Long id;
    private Long tableId;
    private Long stayId;
    private String guestName;
    private String guestPhone;
    private Integer paxCount;
    private Integer splitCount;

    private BillingType billingType;
    private Long servedById;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private SessionStatus status;
    private BigDecimal totalAmount;
}
