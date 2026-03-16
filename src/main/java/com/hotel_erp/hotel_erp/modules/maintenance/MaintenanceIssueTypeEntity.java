package com.hotel_erp.hotel_erp.modules.maintenance;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "maintenance_issue_types")
@EqualsAndHashCode(callSuper = true)
public class MaintenanceIssueTypeEntity extends BaseEntity {
    @Column(unique = true)
    private String name;
    private boolean active = true;
}
