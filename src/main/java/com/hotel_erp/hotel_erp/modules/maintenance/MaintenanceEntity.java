package com.hotel_erp.hotel_erp.modules.maintenance;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "maintenance_requests")
@EqualsAndHashCode(callSuper = true)
public class MaintenanceEntity extends BaseEntity {

    private Long roomId;
    private Long venueId;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private MaintenancePriority priority;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;

    private Long issueTypeId;
    private Long assignedTo;
    private Long reportedBy;

    private LocalDateTime scheduledAt;
    private LocalDateTime completedAt;
    private LocalDateTime resolvedAt;

    @Column(columnDefinition = "TEXT")
    private String resolutionNotes;
}
