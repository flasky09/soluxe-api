package com.hotel_erp.hotel_erp.modules.folio;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "folios")
@EqualsAndHashCode(callSuper = true)
public class FolioEntity extends BaseEntity {

    private Long stayId;
    private Long reservationId;
    private Long venueBookingId;
    private Long diningSessionId;

    @Enumerated(EnumType.STRING)
    private FolioType folioType;

    @Enumerated(EnumType.STRING)
    private FolioStatus status;

    private LocalDateTime openedAt;
    private LocalDateTime closedAt;

    private BigDecimal totalAmount = BigDecimal.ZERO;
}
