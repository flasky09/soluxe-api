package com.hotel_erp.hotel_erp.modules.hotel;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "hotel_settings")
public class HotelSettingsEntity extends BaseEntity {
    private boolean allowWalkIn;
    private boolean allowComplimentary;

    // Profile & Legal
    private String kraPin;
    private String vatStatus;
    private String companyReg;
    private String currency; // e.g. "KES"
    private String website;

    // Operations
    private String checkInTime; // e.g. "14:00"
    private String checkOutTime; // e.g. "10:00"

    // Taxes
    private java.math.BigDecimal vatPercentage;
    private java.math.BigDecimal serviceChargePercentage;
    private java.math.BigDecimal tourismLevyPercentage;
}
