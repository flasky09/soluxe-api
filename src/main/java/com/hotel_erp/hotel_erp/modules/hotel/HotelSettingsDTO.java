package com.hotel_erp.hotel_erp.modules.hotel;

import lombok.Data;

@Data
public class HotelSettingsDTO {
    private Long id;
    private boolean allowWalkIn;
    private boolean allowComplimentary;

    // Profile & Legal
    private String kraPin;
    private String vatStatus;
    private String companyReg;
    private String currency;
    private String website;

    // Operations
    private String checkInTime;
    private String checkOutTime;

    // Taxes
    private java.math.BigDecimal vatPercentage;
    private java.math.BigDecimal serviceChargePercentage;
    private java.math.BigDecimal tourismLevyPercentage;
}
