package com.hotel_erp.hotel_erp.modules.maintenance;

import com.hotel_erp.hotel_erp.shared.BaseService;
import java.util.List;

public interface MaintenanceService extends BaseService<MaintenanceEntity, Long> {
    MaintenanceDTO reportIssue(MaintenanceDTO dto, Long userId);
    MaintenanceDTO assignTicket(Long ticketId, Long assigneeId);
    MaintenanceDTO updateStatus(Long ticketId, MaintenanceStatus status, String resolutionNotes);
    List<MaintenanceDTO> getTicketsByRoom(Long roomId);
    List<MaintenanceDTO> getTicketsByStatus(MaintenanceStatus status);
}
