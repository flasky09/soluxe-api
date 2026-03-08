package com.hotel_erp.hotel_erp.modules.stay;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import com.hotel_erp.hotel_erp.shared.enums.BusinessSource;
import com.hotel_erp.hotel_erp.shared.enums.PlanCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "stays")
@EqualsAndHashCode(callSuper = true)
public class StayEntity extends BaseEntity {

    private Long reservationId;
    
    @Column(nullable = false)
    private Long guestId;
    
    @Column(nullable = false)
    private Long roomId;
    
    @Enumerated(EnumType.STRING)
    private PlanCode planCode;
    
    private BigDecimal ratePerNight;
    
    @Column(nullable = false)
    private LocalDateTime dateIn;
    
    private LocalDateTime dateOut;
    
    private LocalDateTime actualDateOut;
    
    private Integer adults;
    
    private Integer children;
    
    private Boolean isComplimentary = false;
    
    @Enumerated(EnumType.STRING)
    private BusinessSource businessSource;
    
    private String arrivingFrom;
    
    private String nextDestination;
    
    private String arrivalFlightNo;
    
    private String departureFlightNo;
    
    private Boolean cardEncoded = false;
    
    private Long checkedInBy;
    
    private Long checkedOutBy;
    
    @Enumerated(EnumType.STRING)
    private StayStatus status;
    
    private String notes;
}
