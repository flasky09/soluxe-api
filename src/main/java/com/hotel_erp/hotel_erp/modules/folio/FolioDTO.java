package com.hotel_erp.hotel_erp.modules.folio;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FolioDTO {
    private Long id;
    private Long stayId;
    private Long venueBookingId;
    private Long diningSessionId;
    private FolioType folioType;
    private FolioStatus status;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private BigDecimal totalAmount;
}
