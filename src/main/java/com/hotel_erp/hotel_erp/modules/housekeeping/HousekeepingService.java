package com.hotel_erp.hotel_erp.modules.housekeeping;

import com.hotel_erp.hotel_erp.modules.room.RoomEntity;
import com.hotel_erp.hotel_erp.modules.room.RoomRepository;
import com.hotel_erp.hotel_erp.modules.room.RoomStatus;
import com.hotel_erp.hotel_erp.modules.stay.StayRepository;
import com.hotel_erp.hotel_erp.modules.stay.StayStatus;
import com.hotel_erp.hotel_erp.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HousekeepingService {
    private final HousekeepingLogRepository housekeepingLogRepository;
    private final RoomRepository roomRepository;
    private final StayRepository stayRepository;

    public List<RoomEntity> getRoomsNeedingAttention() {
        // Tenant isolation is handled automatically by Hibernate filters
        return roomRepository.findAllByStatusIn(
                Arrays.asList(RoomStatus.DIRTY, RoomStatus.MAINTENANCE)
        );
    }

    @Transactional
    public HousekeepingLogEntity logAction(HousekeepingActionDTO actionDTO) {
        RoomEntity room = roomRepository.findById(actionDTO.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Tenant isolation check is implicit via Hibernate filter on findById

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = userDetails.getId();

        HousekeepingLogEntity log = new HousekeepingLogEntity();
        log.setRoomId(room.getId());
        log.setAction(actionDTO.getAction());
        log.setNotes(actionDTO.getNotes());
        log.setPerformedBy(currentUserId);
        log.setPerformedAt(LocalDateTime.now());

        housekeepingLogRepository.save(log);

        // Update room status based on the action
        updateRoomStatus(room, actionDTO.getAction());

        return log;
    }

    private void updateRoomStatus(RoomEntity room, HousekeepingAction action) {
        switch (action) {
            case CLEANED:
                if (stayRepository.countByRoomIdAndStatus(room.getId(), StayStatus.ACTIVE) > 0) {
                    room.setStatus(RoomStatus.OCCUPIED);
                } else {
                    room.setStatus(RoomStatus.AVAILABLE);
                }
                break;
            case MAINTENANCE_REQUESTED:
                room.setStatus(RoomStatus.MAINTENANCE);
                break;
            case MAINTENANCE_RESOLVED:
                room.setStatus(RoomStatus.AVAILABLE); // Default to available after maintenance
                break;
            case DO_NOT_DISTURB:
                room.setStatus(RoomStatus.OCCUPIED);
                break;
            case INSPECTED:
                // Typically just a log entry, no status change unless specific business rules apply
                break;
            default:
                break;
        }
        roomRepository.save(room);
    }
}
