package com.hotel_erp.hotel_erp.modules.employee.leave;

import lombok.Data;

@Data
public class LeaveTypeDTO {
    private Long id;
    private String name;
    private boolean active;
}
