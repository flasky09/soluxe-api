package com.hotel_erp.hotel_erp.modules.maintenance;

import com.hotel_erp.hotel_erp.modules.room.RoomRepository;
import com.hotel_erp.hotel_erp.modules.room.RoomStatus;
import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceServiceImpl extends BaseServiceImpl<MaintenanceEntity, Long, MaintenanceRepository> implements MaintenanceService {

    private final MaintenanceMapper mapper;
    private final RoomRepository roomRepository;
    private final MaintenanceIssueTypeRepository issueTypeRepository;

    public MaintenanceServiceImpl(MaintenanceRepository repository, 
                                  MaintenanceMapper mapper, 
                                  RoomRepository roomRepository,
                                  MaintenanceIssueTypeRepository issueTypeRepository) {
        super(repository);
        this.mapper = mapper;
        this.roomRepository = roomRepository;
        this.issueTypeRepository = issueTypeRepository;
    }

    @Override
    @Transactional
    public MaintenanceDTO reportIssue(MaintenanceDTO dto, Long userId) {
        MaintenanceEntity entity = mapper.toEntity(dto);
        entity.setReportedBy(userId);
        entity.setStatus(MaintenanceStatus.OPEN);

        if (dto.getIssueTypeId() != null) {
            issueTypeRepository.findById(dto.getIssueTypeId())
                    .ifPresent(entity::setIssueType);
        }
        
        if (entity.getPriority() == null) {
            entity.setPriority(MaintenancePriority.MEDIUM);
        }

        entity = repository.save(entity);

        // Auto-update room status if HIGH or URGENT
        if (entity.getRoomId() != null && 
            (entity.getPriority() == MaintenancePriority.HIGH || entity.getPriority() == MaintenancePriority.URGENT)) {
            
            roomRepository.findById(entity.getRoomId()).ifPresent(room -> {
                room.setStatus(RoomStatus.MAINTENANCE);
                roomRepository.save(room);
            });
        }

        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public MaintenanceDTO assignTicket(Long ticketId, Long assigneeId) {
        MaintenanceEntity entity = repository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Maintenance ticket not found"));
        
        entity.setAssignedTo(assigneeId);
        if (entity.getStatus() == MaintenanceStatus.OPEN) {
            entity.setStatus(MaintenanceStatus.IN_PROGRESS);
        }
        
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public MaintenanceDTO updateStatus(Long ticketId, MaintenanceStatus status, String resolutionNotes) {
        MaintenanceEntity entity = repository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Maintenance ticket not found"));
        
        entity.setStatus(status);
        
        if (status == MaintenanceStatus.RESOLVED) {
            entity.setResolvedAt(LocalDateTime.now());
            entity.setResolutionNotes(resolutionNotes);
            
            // Revert room status to DIRTY for housekeeping
            if (entity.getRoomId() != null) {
                roomRepository.findById(entity.getRoomId()).ifPresent(room -> {
                    if (room.getStatus() == RoomStatus.MAINTENANCE) {
                        room.setStatus(RoomStatus.DIRTY);
                        roomRepository.save(room);
                    }
                });
            }
        } else if (status == MaintenanceStatus.CANCELLED) {
            if (entity.getRoomId() != null) {
                roomRepository.findById(entity.getRoomId()).ifPresent(room -> {
                    if (room.getStatus() == RoomStatus.MAINTENANCE) {
                        room.setStatus(RoomStatus.DIRTY);
                        roomRepository.save(room);
                    }
                });
            }
        }
        
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public List<MaintenanceDTO> getTicketsByRoom(Long roomId) {
        return repository.findAllByRoomId(roomId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaintenanceDTO> getTicketsByStatus(MaintenanceStatus status) {
        return repository.findAllByStatus(status).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    // Override findAll to return DTOs if needed by controller, but we usually map in controller
}
