package com.hotel_erp.hotel_erp.modules.folio;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FolioDTO {
    private Long id;
    private Long stayId;
    private Long reservationId;
    private Long venueBookingId;
    private Long diningSessionId;
    private FolioType folioType;
    private FolioStatus status;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private BigDecimal totalAmount;
    private String guestName;
    private String roomNumber;
    private Long createdBy;
    private Long modifiedBy;
    private String createdByName;
    private String modifiedByName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
