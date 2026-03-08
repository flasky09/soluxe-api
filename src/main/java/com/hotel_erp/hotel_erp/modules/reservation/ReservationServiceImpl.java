package com.hotel_erp.hotel_erp.modules.reservation;

import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import org.springframework.stereotype.Service;

import com.hotel_erp.hotel_erp.modules.guest.GuestRepository;
import com.hotel_erp.hotel_erp.modules.guest.GuestEntity;
import com.hotel_erp.hotel_erp.modules.guest.IdType;
import org.springframework.transaction.annotation.Transactional;

import com.hotel_erp.hotel_erp.modules.room.RoomRepository;
import com.hotel_erp.hotel_erp.modules.room.RoomStatus;
import com.hotel_erp.hotel_erp.modules.room.RoomEntity;

@Service
public class ReservationServiceImpl extends BaseServiceImpl<ReservationEntity, Long, ReservationRepository> implements ReservationService {
    
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final ReservationMapper reservationMapper;

    public ReservationServiceImpl(ReservationRepository repository, GuestRepository guestRepository, RoomRepository roomRepository, ReservationMapper reservationMapper) {
        super(repository);
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.reservationMapper = reservationMapper;
    }

    @Override
    @Transactional
    public ReservationDTO createFromDto(ReservationDTO dto) {
        // Update guest info if provided
        if (dto.getGuestId() != null) {
            guestRepository.findById(dto.getGuestId()).ifPresent(guest -> {
                if (dto.getNationality() != null) guest.setNationality(dto.getNationality());
                if (dto.getIdType() != null) {
                    try {
                        guest.setIdType(IdType.valueOf(dto.getIdType()));
                    } catch (IllegalArgumentException ignored) {}
                }
                if (dto.getIdNumber() != null) guest.setIdNumber(dto.getIdNumber());
                if (dto.getPreferences() != null) guest.setPreferences(dto.getPreferences());
                if (dto.getVehicleRegistration() != null) guest.setVehicleRegistration(dto.getVehicleRegistration());
                if (dto.getEmergencyContactName() != null) guest.setEmergencyContactName(dto.getEmergencyContactName());
                if (dto.getEmergencyContactPhone() != null) guest.setEmergencyContactPhone(dto.getEmergencyContactPhone());
                guestRepository.save(guest);
            });
        }

        ReservationEntity entity = reservationMapper.toEntity(dto);
        if (entity.getStatus() == null) {
            entity.setStatus(ReservationStatus.BOOKED);
        }
        return reservationMapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public ReservationDTO checkIn(Long reservationId, Long roomId) {
        ReservationEntity reservation = repository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        
        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new RuntimeException("Room is not available for check-in");
        }

        reservation.setStatus(ReservationStatus.CHECKED_IN);
        reservation.setRoomId(roomId);
        room.setStatus(RoomStatus.OCCUPIED);
        
        roomRepository.save(room);
        return reservationMapper.toDto(repository.save(reservation));
    }

    @Override
    @Transactional
    public ReservationDTO checkOut(Long reservationId) {
        ReservationEntity reservation = repository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getRoomId() != null) {
            roomRepository.findById(reservation.getRoomId()).ifPresent(room -> {
                room.setStatus(RoomStatus.DIRTY);
                roomRepository.save(room);
            });
        }

        reservation.setStatus(ReservationStatus.CHECKED_OUT);
        return reservationMapper.toDto(repository.save(reservation));
    }

    @Override
    @Transactional
    public ReservationDTO cancel(Long reservationId) {
        ReservationEntity reservation = repository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setStatus(ReservationStatus.CANCELLED);
        return reservationMapper.toDto(repository.save(reservation));
    }
}
