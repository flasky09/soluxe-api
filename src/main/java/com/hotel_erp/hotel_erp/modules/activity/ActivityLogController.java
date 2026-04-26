package com.hotel_erp.hotel_erp.modules.activity;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    @GetMapping
    public List<ActivityLogDTO> getAllLogs(@RequestParam(value = "userId", required = false) Long userId) {
        if (userId != null) {
            return activityLogService.getActivityLogsByUser(userId);
        }
        return activityLogService.getAllActivityLogs();
    }
}
