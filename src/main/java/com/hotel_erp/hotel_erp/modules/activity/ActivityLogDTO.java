package com.hotel_erp.hotel_erp.modules.activity;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ActivityLogDTO {
    private Long id;
    private Long userId;
    private String username;
    private String action;
    private String description;
    private LocalDateTime timestamp;
}
