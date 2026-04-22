package com.hotel_erp.hotel_erp.modules.folio;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "folios")
@EqualsAndHashCode(callSuper = true)
public class FolioEntity extends BaseEntity {

    private Long stayId;
    private Long reservationId;
    private Long venueBookingId;
    private Long diningSessionId;

    @Version
    @Builder.Default
    private Long version = 0L;

    @Enumerated(EnumType.STRING)
    private FolioType folioType;

    @Enumerated(EnumType.STRING)
    private FolioStatus status;

    private LocalDateTime openedAt;
    private LocalDateTime closedAt;

    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;
}
