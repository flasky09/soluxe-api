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

    public Long getStayId() {
        return stayId;
    }

    public void setStayId(Long stayId) {
        this.stayId = stayId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getVenueBookingId() {
        return venueBookingId;
    }

    public void setVenueBookingId(Long venueBookingId) {
        this.venueBookingId = venueBookingId;
    }

    public Long getDiningSessionId() {
        return diningSessionId;
    }

    public void setDiningSessionId(Long diningSessionId) {
        this.diningSessionId = diningSessionId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public FolioType getFolioType() {
        return folioType;
    }

    public void setFolioType(FolioType folioType) {
        this.folioType = folioType;
    }

    public FolioStatus getStatus() {
        return status;
    }

    public void setStatus(FolioStatus status) {
        this.status = status;
    }

    public java.time.LocalDateTime getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(java.time.LocalDateTime openedAt) {
        this.openedAt = openedAt;
    }

    public java.time.LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(java.time.LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
