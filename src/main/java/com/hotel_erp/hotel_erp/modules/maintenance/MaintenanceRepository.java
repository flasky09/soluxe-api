package com.hotel_erp.hotel_erp.modules.maintenance;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRepository extends BaseRepository<MaintenanceEntity, Long> {
    List<MaintenanceEntity> findAllByStatus(MaintenanceStatus status);

    List<MaintenanceEntity> findAllByRoomId(Long roomId);

    List<MaintenanceEntity> findAllByAssignedTo(Long assignedTo);
}
