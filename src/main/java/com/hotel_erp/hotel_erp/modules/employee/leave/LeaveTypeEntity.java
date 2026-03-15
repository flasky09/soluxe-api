package com.hotel_erp.hotel_erp.modules.employee.leave;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "leave_types")
@EqualsAndHashCode(callSuper = true)
public class LeaveTypeEntity extends BaseEntity {
    @Column(unique = true)
    private String name;
    private String description;
    private boolean active = true;
}
