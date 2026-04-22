package com.hotel_erp.hotel_erp.modules.venue;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
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
