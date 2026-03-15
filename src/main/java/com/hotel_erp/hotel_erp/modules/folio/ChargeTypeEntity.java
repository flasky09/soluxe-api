package com.hotel_erp.hotel_erp.modules.folio;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "charge_types")
@EqualsAndHashCode(callSuper = true)
public class ChargeTypeEntity extends BaseEntity {
    @Column(unique = true)
    private String name;
    private String description;
    private boolean active = true;
}
