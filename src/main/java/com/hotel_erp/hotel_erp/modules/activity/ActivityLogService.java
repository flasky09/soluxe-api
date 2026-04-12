package com.hotel_erp.hotel_erp.modules.activity;

import java.util.List;

public interface ActivityLogService {
    void logActivity(Long userId, String action, String description);
    
    List<ActivityLogDTO> getAllActivityLogs();
}
