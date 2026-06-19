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
@Table(name = "folio_charges")
@EqualsAndHashCode(callSuper = true)
public class FolioChargeEntity extends BaseEntity {

    @Column(nullable = false)
    private Long folioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_type_id")
    private ChargeTypeEntity chargeType;

    private String description;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    @Builder.Default
    private BigDecimal discountPct = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal taxPct = BigDecimal.ZERO;
    private BigDecimal totalAmount;

    private LocalDateTime chargedAt;
    private Long addedBy;

    private String referenceId;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private boolean voided;
    private String voidReason;
    private Long voidedBy;

    public void setFolioId(Long folioId) {
        this.folioId = folioId;
    }

    public void setChargedAt(LocalDateTime chargedAt) {
        this.chargedAt = chargedAt;
    }

    public void setAddedBy(Long addedBy) {
        this.addedBy = addedBy;
    }
}
