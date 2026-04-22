package com.hotel_erp.hotel_erp.modules.maintenance;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "maintenance_issue_types")
@EqualsAndHashCode(callSuper = true)
public class MaintenanceIssueTypeEntity extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;
    
    private String description;
    
    @Builder.Default
    private boolean active = true;
}
