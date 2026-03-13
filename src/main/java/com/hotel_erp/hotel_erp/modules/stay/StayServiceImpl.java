package com.hotel_erp.hotel_erp.modules.stay;

import com.hotel_erp.hotel_erp.modules.reservation.ReservationEntity;
import com.hotel_erp.hotel_erp.modules.reservation.ReservationRepository;
import com.hotel_erp.hotel_erp.modules.reservation.ReservationStatus;
import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class StayServiceImpl extends BaseServiceImpl<StayEntity, Long, StayRepository> implements StayService {

    private final ReservationRepository reservationRepository;
    private final StayMapper stayMapper;
    private final com.hotel_erp.hotel_erp.modules.folio.FolioService folioService;
    private final com.hotel_erp.hotel_erp.modules.folio.FolioRepository folioRepository;
    private final com.hotel_erp.hotel_erp.modules.room.RoomRepository roomRepository;
    private final com.hotel_erp.hotel_erp.modules.folio.ChargeTypeRepository chargeTypeRepository;

    public StayServiceImpl(StayRepository repository,
                           ReservationRepository reservationRepository,
                           StayMapper stayMapper,
                           com.hotel_erp.hotel_erp.modules.folio.FolioService folioService,
                           com.hotel_erp.hotel_erp.modules.folio.FolioRepository folioRepository,
                           com.hotel_erp.hotel_erp.modules.room.RoomRepository roomRepository,
                           com.hotel_erp.hotel_erp.modules.folio.ChargeTypeRepository chargeTypeRepository) {
        super(repository);
        this.reservationRepository = reservationRepository;
        this.stayMapper = stayMapper;
        this.folioService = folioService;
        this.folioRepository = folioRepository;
        this.roomRepository = roomRepository;
        this.chargeTypeRepository = chargeTypeRepository;
    }

    @Override
    @Transactional
    public StayDTO checkIn(Long reservationId, Long roomId, Long userId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.BOOKED) {
             throw new RuntimeException("Reservation is not in BOOKED status");
        }

        StayEntity stay = new StayEntity();
        stay.setReservationId(reservationId);
        stay.setGuestId(reservation.getGuestId());
        stay.setRoomId(roomId);
        stay.setDateIn(LocalDateTime.now());
        stay.setDateOut(reservation.getDateOut() != null ? reservation.getDateOut().atStartOfDay() : null);
        stay.setAdults(reservation.getAdults());
        stay.setChildren(reservation.getChildren());
        stay.setStatus(StayStatus.ACTIVE);
        stay.setCheckedInBy(userId);
        
        stay = repository.save(stay);
        
        // Update Reservation Status
        reservation.setStatus(ReservationStatus.CHECKED_IN);
        reservation.setRoomId(roomId);
        reservationRepository.save(reservation);

        // Update Room Status
        roomRepository.findById(roomId).ifPresent(room -> {
            room.setStatus(com.hotel_erp.hotel_erp.modules.room.RoomStatus.OCCUPIED);
            roomRepository.save(room);
        });

        // Create Folio for the Stay
        com.hotel_erp.hotel_erp.modules.folio.FolioDTO folio = folioService.createFolioForStay(stay.getId());

        // Auto-post Room Charge (Advance Billing)
        postRoomCharge(stay, folio.getId(), userId);

        return stayMapper.toDto(stay);
    }

    @Override
    @Transactional
    public StayDTO checkOut(Long stayId, Long userId) {
        StayEntity stay = repository.findById(stayId)
                .orElseThrow(() -> new RuntimeException("Stay not found"));

        if (stay.getStatus() != StayStatus.ACTIVE) {
            throw new RuntimeException("Stay is not ACTIVE");
        }

        // ✅ VALIDATE FOLIO BALANCE FIRST — before any mutations
        var folio = folioRepository.findByStayId(stayId).orElse(null);
        if (folio != null && folio.getTotalAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST,
                "Cannot check out. Outstanding folio balance: " + folio.getTotalAmount()
            );
        }

        // Mutate and persist stay
        stay.setStatus(StayStatus.CHECKED_OUT);
        stay.setActualDateOut(LocalDateTime.now());
        stay.setCheckedOutBy(userId);
        stay = repository.save(stay);

        // Update Room Status to DIRTY
        if (stay.getRoomId() != null) {
            roomRepository.findById(stay.getRoomId()).ifPresent(room -> {
                room.setStatus(com.hotel_erp.hotel_erp.modules.room.RoomStatus.DIRTY);
                roomRepository.save(room);
            });
        }

        // Update Reservation Status (if linked)
        if (stay.getReservationId() != null) {
            reservationRepository.findById(stay.getReservationId()).ifPresent(res -> {
                res.setStatus(ReservationStatus.CHECKED_OUT);
                reservationRepository.save(res);
            });
        }

        // Close Folio
        if (folio != null) {
            folioService.closeFolio(folio.getId(), userId);
        }

        return stayMapper.toDto(stay);
    }

    @Override
    @Transactional
    public StayDTO walkInCheckIn(Long guestId, Long roomId, Integer adults, Integer children, Long userId) {
        var room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));
        if (room.getStatus() != com.hotel_erp.hotel_erp.modules.room.RoomStatus.AVAILABLE) {
            throw new RuntimeException("Room " + room.getRoomNumber() + " is not available for check-in.");
        }

        StayEntity stay = new StayEntity();
        stay.setReservationId(null);
        stay.setGuestId(guestId);
        stay.setRoomId(roomId);
        stay.setDateIn(LocalDateTime.now());
        stay.setAdults(adults != null ? adults : 1);
        stay.setChildren(children != null ? children : 0);
        stay.setStatus(StayStatus.ACTIVE);
        stay.setCheckedInBy(userId);

        stay = repository.save(stay);

        // Update Room Status to OCCUPIED
        room.setStatus(com.hotel_erp.hotel_erp.modules.room.RoomStatus.OCCUPIED);
        roomRepository.save(room);

        // Create Folio for the Stay
        com.hotel_erp.hotel_erp.modules.folio.FolioDTO folio = folioService.createFolioForStay(stay.getId());

        // Auto-post Room Charge (Advance Billing)
        postRoomCharge(stay, folio.getId(), userId);

        return stayMapper.toDto(stay);
    }

    @Override
    @Transactional
    public StayDTO checkOutByReservationId(Long reservationId, Long userId) {
        StayEntity stay = repository.findByReservationIdAndStatus(reservationId, StayStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Active Stay not found for this reservation"));
        return checkOut(stay.getId(), userId);
    }

    private void postRoomCharge(StayEntity stay, Long folioId, Long userId) {
        long nights = 0;
        if (stay.getDateIn() != null && stay.getDateOut() != null) {
            nights = ChronoUnit.DAYS.between(stay.getDateIn().toLocalDate(), stay.getDateOut().toLocalDate());
        }
        if (nights <= 0) nights = 1; // Minimum 1 night charge

        var room = roomRepository.findById(stay.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        
        BigDecimal rate = stay.getRatePerNight() != null ? stay.getRatePerNight() : room.getRoomType().getDefaultRate();
        if (rate == null) rate = BigDecimal.ZERO;

        com.hotel_erp.hotel_erp.modules.folio.FolioChargeDTO chargeDto = com.hotel_erp.hotel_erp.modules.folio.FolioChargeDTO.builder()
                .folioId(folioId)
                .description("Room Charge - " + nights + " Night(s)")
                .quantity(new BigDecimal(nights))
                .unitPrice(rate)
                .taxPct(BigDecimal.ZERO)
                .discountPct(BigDecimal.ZERO)
                .addedBy(userId)
                .build();

        // Try to find or create "Room Charge" type
        chargeTypeRepository.findByName("Room Charge").ifPresent(type -> {
            chargeDto.setChargeTypeId(type.getId());
        });

        folioService.addCharge(folioId, chargeDto, userId);
    }
}

