package com.hotel_erp.hotel_erp.modules.reservation;

import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import com.hotel_erp.hotel_erp.modules.guest.GuestRepository;
import com.hotel_erp.hotel_erp.modules.guest.IdType;
import org.springframework.transaction.annotation.Transactional;

import com.hotel_erp.hotel_erp.modules.stay.StayRepository;
import com.hotel_erp.hotel_erp.modules.stay.StayStatus;

@Service
public class ReservationServiceImpl extends BaseServiceImpl<ReservationEntity, Long, ReservationRepository> implements ReservationService {
    
    private final GuestRepository guestRepository;
    private final StayRepository stayRepository;
    private final ReservationMapper reservationMapper;
    private final com.hotel_erp.hotel_erp.modules.room.RoomRepository roomRepository;
    private final com.hotel_erp.hotel_erp.modules.room.RoomStatus roomStatus = com.hotel_erp.hotel_erp.modules.room.RoomStatus.AVAILABLE;

    public ReservationServiceImpl(ReservationRepository repository,
                                  GuestRepository guestRepository,
                                  StayRepository stayRepository,
                                  ReservationMapper reservationMapper,
                                  com.hotel_erp.hotel_erp.modules.room.RoomRepository roomRepository) {
        super(repository);
        this.guestRepository = guestRepository;
        this.stayRepository = stayRepository;
        this.reservationMapper = reservationMapper;
        this.roomRepository = roomRepository;
    }

    @Override
    @Transactional
    public ReservationDTO createFromDto(ReservationDTO dto) {
        // Update guest info if provided
        if (dto.getGuestId() != null) {
            guestRepository.findById(dto.getGuestId()).ifPresent(guest -> {
                if (dto.getNationality() != null) {
                    guest.setNationality(dto.getNationality());
                }
                if (dto.getIdType() != null) {
                    try {
                        guest.setIdType(IdType.valueOf(dto.getIdType()));
                    } catch (IllegalArgumentException e) {
                        // ignore
                    }
                }
                if (dto.getIdNumber() != null) {
                    guest.setIdNumber(dto.getIdNumber());
                }
                if (dto.getPreferences() != null) {
                    guest.setPreferences(dto.getPreferences());
                }
                if (dto.getVehicleRegistration() != null) {
                    guest.setVehicleRegistration(dto.getVehicleRegistration());
                }
                if (dto.getEmergencyContactName() != null) {
                    guest.setEmergencyContactName(dto.getEmergencyContactName());
                }
                if (dto.getEmergencyContactPhone() != null) {
                    guest.setEmergencyContactPhone(dto.getEmergencyContactPhone());
                }
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
    public ReservationDTO cancel(Long reservationId) {
        ReservationEntity reservation = repository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setStatus(ReservationStatus.CANCELLED);
        repository.save(reservation);

        // BUG 7 FIX: Release the room back to AVAILABLE when a reservation is cancelled.
        if (reservation.getRoomId() != null) {
            roomRepository.findById(reservation.getRoomId()).ifPresent(room -> {
                if (room.getStatus() == com.hotel_erp.hotel_erp.modules.room.RoomStatus.OCCUPIED) {
                    room.setStatus(com.hotel_erp.hotel_erp.modules.room.RoomStatus.AVAILABLE);
                    roomRepository.save(room);
                }
            });
        }

        return reservationMapper.toDto(reservation);
    }

    @Override
    @Transactional
    public ReservationDTO updateReservation(Long id, ReservationDTO dto) {
        ReservationEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (existing.getDateOut() != null && dto.getDateOut() != null && !existing.getDateOut().equals(dto.getDateOut())) {
            stayRepository.findByReservationIdAndStatus(id, StayStatus.ACTIVE)
                    .ifPresent(stay -> {
                        stay.setDateOut(dto.getDateOut().atStartOfDay());
                        stayRepository.save(stay);
                    });
        }

        if (dto.getRoomTypeId() != null) {
            existing.setRoomTypeId(dto.getRoomTypeId());
        }
        if (dto.getDateIn() != null) {
            existing.setDateIn(dto.getDateIn());
        }
        if (dto.getDateOut() != null) {
            existing.setDateOut(dto.getDateOut());
        }
        existing.setAdults(dto.getAdults());
        existing.setChildren(dto.getChildren());
        if (dto.getTableId() != null) {
            existing.setTableId(dto.getTableId());
        }
        if (dto.getTableReservationTime() != null) {
            existing.setTableReservationTime(dto.getTableReservationTime());
        }
        if (dto.getTablePax() != null) {
            existing.setTablePax(dto.getTablePax());
        }
        if (dto.getEta() != null && !dto.getEta().isEmpty()) {
            existing.setEta(java.time.LocalTime.parse(dto.getEta()));
        }
        if (dto.getEtd() != null && !dto.getEtd().isEmpty()) {
            existing.setEtd(java.time.LocalTime.parse(dto.getEtd()));
        }
        if (dto.getSpecialRequests() != null) {
            existing.setSpecialRequests(dto.getSpecialRequests());
        }

        if (dto.getGuestId() != null) {
            existing.setGuestId(dto.getGuestId());
            guestRepository.findById(dto.getGuestId()).ifPresent(guest -> {
                if (dto.getNationality() != null) {
                    guest.setNationality(dto.getNationality());
                }
                if (dto.getIdType() != null) {
                    try {
                        guest.setIdType(IdType.valueOf(dto.getIdType()));
                    } catch (IllegalArgumentException e) {
                        // ignore
                    }
                }
                if (dto.getIdNumber() != null) {
                    guest.setIdNumber(dto.getIdNumber());
                }
                if (dto.getPreferences() != null) {
                    guest.setPreferences(dto.getPreferences());
                }
                if (dto.getVehicleRegistration() != null) {
                    guest.setVehicleRegistration(dto.getVehicleRegistration());
                }
                if (dto.getEmergencyContactName() != null) {
                    guest.setEmergencyContactName(dto.getEmergencyContactName());
                }
                if (dto.getEmergencyContactPhone() != null) {
                    guest.setEmergencyContactPhone(dto.getEmergencyContactPhone());
                }
                guestRepository.save(guest);
            });
        }

        return reservationMapper.toDto(repository.save(existing));
    }

    @Override
    public List<ReservationDTO> getTodayArrivals() {
        // BUG 8 FIX: Filter by today's date so only arrivals for today are returned.
        java.time.LocalDate today = java.time.LocalDate.now();
        return repository.findByStatusAndDateIn(ReservationStatus.BOOKED, today)
                .stream()
                .map(reservationMapper::toDto)
                .collect(java.util.stream.Collectors.toList());
    }
}
