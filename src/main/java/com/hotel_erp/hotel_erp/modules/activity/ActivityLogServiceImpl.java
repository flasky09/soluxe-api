package com.hotel_erp.hotel_erp.modules.activity;

import com.hotel_erp.hotel_erp.modules.user.UserEntity;
import com.hotel_erp.hotel_erp.modules.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository repository;
    private final UserRepository userRepository;

    @Override
    public void logActivity(Long userId, String action, String description) {
        String username = "Unknown";
        if (userId != null) {
            username = userRepository.findById(userId)
                    .map(UserEntity::getUsername)
                    .orElse("Unknown");
        }

        ActivityLog log = ActivityLog.builder()
                .userId(userId != null ? userId : -1L)
                .username(username)
                .action(action)
                .description(description)
                .build();
        repository.save(log);
    }

    @Override
    public List<ActivityLogDTO> getAllActivityLogs() {
        return repository.findAllByOrderByTimestampDesc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActivityLogDTO> getActivityLogsByUser(Long userId) {
        return repository.findByUserIdOrderByTimestampDesc(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ActivityLogDTO toDto(ActivityLog log) {
        return ActivityLogDTO.builder()
                .id(log.getId())
                .userId(log.getUserId())
                .username(log.getUsername())
                .action(log.getAction())
                .description(log.getDescription())
                .timestamp(log.getTimestamp())
                .build();
    }
}
