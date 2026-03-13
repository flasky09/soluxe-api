package com.hotel_erp.hotel_erp.modules.maintenance;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MaintenanceDTO {
    private Long id;
    private Long roomId;
    private Long reportedBy;
    private Long assignedTo;
    private Long issueTypeId;
    private String issueTypeName;
    private String description;
    private MaintenanceStatus status;
    private MaintenancePriority priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    private String resolutionNotes;
}
