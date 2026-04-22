package com.hotel_erp.hotel_erp.modules.room;

import com.hotel_erp.hotel_erp.modules.maintenance.MaintenanceRepository;
import com.hotel_erp.hotel_erp.modules.reservation.ReservationRepository;
import com.hotel_erp.hotel_erp.modules.stay.StayRepository;
import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class RoomServiceImpl extends BaseServiceImpl<RoomEntity, Long, RoomRepository> implements RoomService {
    
    private final RoomTypeRepository roomTypeRepository;
    private final StayRepository stayRepository;
    private final ReservationRepository reservationRepository;
    private final MaintenanceRepository maintenanceRepository;

    public RoomServiceImpl(RoomRepository repository, 
                           RoomTypeRepository roomTypeRepository,
                           StayRepository stayRepository,
                           ReservationRepository reservationRepository,
                           MaintenanceRepository maintenanceRepository) {
        super(repository);
        this.roomTypeRepository = roomTypeRepository;
        this.stayRepository = stayRepository;
        this.reservationRepository = reservationRepository;
        this.maintenanceRepository = maintenanceRepository;
    }

    @Override
    public RoomEntity save(RoomEntity entity) {
        if (entity.getRoomType() != null && entity.getRoomType().getId() != null) {
            roomTypeRepository.findById(entity.getRoomType().getId()).ifPresent(entity::setRoomType);
        }
        return super.save(entity);
    }

    @Override
    public RoomHistoryDTO getRoomHistory(Long roomId, LocalDate date) {
        RoomEntity room = repository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        List<RoomHistoryItemDTO> items = new ArrayList<>();

        // 1. Stays
        stayRepository.findAllByRoomId(roomId).stream()
                .filter(s -> date == null || (s.getDateIn() != null && s.getDateIn().toLocalDate().equals(date)))
                .forEach(s -> {
                    items.add(RoomHistoryItemDTO.builder()
                            .type("STAY")
                            .description("Guest stay #" + s.getId())
                            .timestamp(s.getDateIn())
                            .status(s.getStatus().toString())
                            .build());
                });

        // 2. Reservations
        reservationRepository.findAll().stream()
                .filter(r -> r.getRoomId() != null && r.getRoomId().equals(roomId))
                .filter(r -> date == null || r.getDateIn().equals(date))
                .forEach(r -> {
                    items.add(RoomHistoryItemDTO.builder()
                            .type("RESERVATION")
                            .description("Reservation for guest ID: " + r.getGuestId())
                            .timestamp(r.getDateIn().atStartOfDay())
                            .status(r.getStatus().toString())
                            .build());
                });

        // 3. Maintenance
        maintenanceRepository.findAllByRoomId(roomId).stream()
                .filter(m -> date == null || (m.getCreatedAt() != null && m.getCreatedAt().toLocalDate().equals(date)))
                .forEach(m -> {
                    items.add(RoomHistoryItemDTO.builder()
                            .type("MAINTENANCE")
                            .description("Maintenance ticket: " + m.getDescription())
                            .timestamp(m.getCreatedAt())
                            .status(m.getStatus().toString())
                            .build());
                });

        items.sort(Comparator.comparing(RoomHistoryItemDTO::getTimestamp).reversed());

        return RoomHistoryDTO.builder()
                .roomId(roomId)
                .roomNumber(room.getRoomNumber())
                .items(items)
                .build();
    }

    @Override
    public List<RoomHistoryItemDTO> getRoomCalendar(Long roomId) {
        List<RoomHistoryItemDTO> calendar = new ArrayList<>();

        // Add reservations as blocked dates
        reservationRepository.findAll().stream()
                .filter(r -> r.getRoomId() != null && r.getRoomId().equals(roomId))
                .forEach(r -> {
                    calendar.add(RoomHistoryItemDTO.builder()
                            .type("RESERVATION")
                            .timestamp(r.getDateIn().atStartOfDay())
                            .description("Reserved until " + r.getDateOut())
                            .status(r.getStatus().toString())
                            .build());
                });

        // Add stays
        stayRepository.findAllByRoomId(roomId).forEach(s -> {
            calendar.add(RoomHistoryItemDTO.builder()
                    .type("STAY")
                    .timestamp(s.getDateIn())
                    .description("Occupied until " + s.getDateOut())
                    .status(s.getStatus().toString())
                    .build());
        });

        return calendar;
    }
}
