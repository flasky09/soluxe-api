package com.hotel_erp.hotel_erp.modules.stay;

import com.hotel_erp.hotel_erp.shared.enums.BusinessSource;
import com.hotel_erp.hotel_erp.shared.enums.PlanCode;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StayDTO {
    private Long id;
    private Long reservationId;
    private Long guestId;
    private Long roomId;
    private PlanCode planCode;
    private BigDecimal ratePerNight;
    private LocalDateTime dateIn;
    private LocalDateTime dateOut;
    private LocalDateTime actualDateOut;
    private Integer adults;
    private Integer children;
    private Boolean isComplimentary;
    private BusinessSource businessSource;
    private StayStatus status;
    private Long createdBy;
    private Long modifiedBy;
}
