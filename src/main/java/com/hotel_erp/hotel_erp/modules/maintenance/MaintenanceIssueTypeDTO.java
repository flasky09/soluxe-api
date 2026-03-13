package com.hotel_erp.hotel_erp.modules.maintenance;

import lombok.Data;

@Data
public class MaintenanceIssueTypeDTO {
    private Long id;
    private String name;
    private String description;
    private boolean active;
}
