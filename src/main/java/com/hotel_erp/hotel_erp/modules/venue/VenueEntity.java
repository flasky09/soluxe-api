package com.hotel_erp.hotel_erp.modules.venue;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "venues")
@EqualsAndHashCode(callSuper = true)
public class VenueEntity extends BaseEntity {
    @Column(unique = true)
    private String name;
    
    private String type;
    
    private Integer capacity;
    private BigDecimal ratePerHour;
    private BigDecimal ratePerDay;
    private String description;
}
