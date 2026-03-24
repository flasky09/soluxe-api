package com.hotel_erp.hotel_erp.modules.stay;

import com.hotel_erp.hotel_erp.modules.reservation.ReservationEntity;
import com.hotel_erp.hotel_erp.modules.reservation.ReservationRepository;
import com.hotel_erp.hotel_erp.modules.reservation.ReservationStatus;
import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import com.hotel_erp.hotel_erp.modules.folio.FolioService;
import com.hotel_erp.hotel_erp.modules.folio.FolioRepository;
import com.hotel_erp.hotel_erp.modules.folio.FolioChargeRepository;
import com.hotel_erp.hotel_erp.modules.folio.FolioDTO;
import com.hotel_erp.hotel_erp.modules.folio.FolioChargeDTO;
import com.hotel_erp.hotel_erp.modules.folio.ChargeTypeRepository;
import com.hotel_erp.hotel_erp.modules.master.TenantConfigRepository;
import com.hotel_erp.hotel_erp.modules.room.RoomRepository;
import com.hotel_erp.hotel_erp.modules.room.RoomStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StayServiceImpl extends BaseServiceImpl<StayEntity, Long, StayRepository> implements StayService {

    private final ReservationRepository reservationRepository;
    private final StayMapper stayMapper;
    private final TenantConfigRepository tenantConfigRepository;
    private final FolioService folioService;
    private final FolioRepository folioRepository;
    private final RoomRepository roomRepository;
    private final ChargeTypeRepository chargeTypeRepository;
    private final StayFlagService stayFlagService;
    private final FolioChargeRepository folioChargeRepository;

    public StayServiceImpl(StayRepository repository,
                           ReservationRepository reservationRepository,
                           StayMapper stayMapper,
                           TenantConfigRepository tenantConfigRepository,
                           FolioService folioService,
                           FolioRepository folioRepository,
                           RoomRepository roomRepository,
                           ChargeTypeRepository chargeTypeRepository,
                           StayFlagService stayFlagService,
                           FolioChargeRepository folioChargeRepository) {
        super(repository);
        this.reservationRepository = reservationRepository;
        this.stayMapper = stayMapper;
        this.tenantConfigRepository = tenantConfigRepository;
        this.folioService = folioService;
        this.folioRepository = folioRepository;
        this.roomRepository = roomRepository;
        this.chargeTypeRepository = chargeTypeRepository;
        this.stayFlagService = stayFlagService;
        this.folioChargeRepository = folioChargeRepository;
    }

    @Override
    @Transactional
    public StayDTO checkIn(Long reservationId, Long roomId, Long userId) {
        validateUser(userId);
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.BOOKED) {
             throw new RuntimeException("Reservation is not in BOOKED status");
        }

        var room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));
        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new RuntimeException("Room " + room.getRoomNumber() + " is not available for check-in.");
        }

        // Validate no existing active stay for this guest
        if (repository.countByGuestIdAndStatusIn(reservation.getGuestId(), List.of(StayStatus.ACTIVE, StayStatus.OVERSTAY, StayStatus.DUE_CHECKOUT)) > 0) {
            throw new RuntimeException("Guest already has an active stay.");
        }

        StayEntity stay = new StayEntity();
        stay.setReservationId(reservationId);
        stay.setGuestId(reservation.getGuestId());
        stay.setRoomId(roomId);
        stay.setDateIn(LocalDateTime.now());
        stay.setDateOut(reservation.getDateOut() != null ? reservation.getDateOut().atTime(12, 0) : null);
        stay.setAdults(reservation.getAdults());
        stay.setChildren(reservation.getChildren());
        stay.setStatus(StayStatus.ACTIVE);
        stay.setCheckedInBy(userId);
        
        final StayEntity savedStay = repository.save(stay);
        
        // Update Reservation Status
        reservation.setStatus(ReservationStatus.CHECKED_IN);
        
        // If the requested room is different from the reservation's assigned room, free the old assigned room
        if (reservation.getRoomId() != null && !reservation.getRoomId().equals(roomId)) {
            roomRepository.findById(reservation.getRoomId()).ifPresent(oldRoom -> {
                if (oldRoom.getStatus() == RoomStatus.OCCUPIED) {
                    oldRoom.setStatus(RoomStatus.AVAILABLE);
                    roomRepository.save(oldRoom);
                }
            });
        }
        
        reservation.setRoomId(roomId);
        reservationRepository.save(reservation);

        // Update Room Status
        roomRepository.findById(roomId).ifPresent(r -> {
            r.setStatus(RoomStatus.OCCUPIED);
            roomRepository.save(r);
        });

        // Create or Link Folio for the Stay
        FolioDTO folio = folioRepository.findByReservationId(reservationId)
                .map(f -> folioService.linkReservationFolioToStay(reservationId, savedStay.getId()))
                .orElseGet(() -> folioService.createFolioForStay(savedStay.getId()));

        // Auto-post Room Charge (Advance Billing)
        postRoomCharge(savedStay, folio.getId(), userId);

        return stayMapper.toDto(savedStay);
    }

    @Override
    @Transactional
    public StayDTO checkOut(Long id, Long userId) {
        return checkOut(id, userId, false); // Default to no auto-adjustment without approval UI
    }

    @Override
    @Transactional
    public StayDTO checkOut(Long id, Long userId, boolean approveAdjustment) {
        StayEntity stay = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stay not found"));

        if (stay.getStatus() == StayStatus.CHECKED_OUT) {
            throw new RuntimeException("Stay is already checked out");
        }

        // ✅ VALIDATE FOLIO BALANCE FIRST — before any mutations
        var folio = folioRepository.findByStayId(id).orElse(null);
        
        LocalDateTime now = LocalDateTime.now();
        
        if (folio != null && stay.getDateOut() != null) {
            long plannedNights = ChronoUnit.DAYS.between(stay.getDateIn().toLocalDate(), stay.getDateOut().toLocalDate());
            if (plannedNights < 1) {
                plannedNights = 1;
            }

            long actualNights = ChronoUnit.DAYS.between(stay.getDateIn().toLocalDate(), now.toLocalDate());
            
            // Check-out on same day as Check-in is still 1 night (industry standard)
            if (actualNights < 1) {
                actualNights = 1;
            }

            if (actualNights < plannedNights) {
                long diff = plannedNights - actualNights;
                
                // Fallback rate logic
                BigDecimal rate = stay.getRatePerNight();
                if (rate == null) {
                    var room = roomRepository.findById(stay.getRoomId()).orElse(null);
                    if (room != null && room.getRoomType() != null) {
                        rate = room.getRoomType().getDefaultRate();
                    }
                }
                if (rate == null) {
                    rate = BigDecimal.ZERO;
                }
                
                // Post adjustment
                FolioChargeDTO adjustment = FolioChargeDTO.builder()
                        .folioId(folio.getId())
                        .description("Understay Adjustment - " + diff + " Night(s) (Early Check-out)")
                        .quantity(new BigDecimal(diff))
                        .unitPrice(rate.negate()) // Credit
                        .taxPct(BigDecimal.ZERO)
                        .discountPct(BigDecimal.ZERO)
                        .addedBy(userId)
                        .build();
                
                chargeTypeRepository.findByName("Room Charge").ifPresent(type -> adjustment.setChargeTypeId(type.getId()));
                folioService.addCharge(folio.getId(), adjustment, userId);
                
                // Refresh folio balance after adjustment
                folio = folioRepository.findById(folio.getId())
                        .orElseThrow(() -> new RuntimeException("Folio re-fetch failed after adjustment"));
            }
        }

        if (folio != null) {
            BigDecimal total = folio.getTotalAmount() != null ? folio.getTotalAmount() : BigDecimal.ZERO;
            if (total.abs().compareTo(new BigDecimal("0.01")) > 0) {
                if (total.compareTo(BigDecimal.ZERO) > 0) {
                    throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Cannot check out. Outstanding folio balance: " + total
                    );
                } else {
                    throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Cannot check out. Guest has a credit balance (refund required): " + total
                    );
                }
            }
        }

        // Mutate and persist stay
        stay.setStatus(StayStatus.CHECKED_OUT);
        stay.setActualDateOut(LocalDateTime.now());
        stay.setCheckedOutBy(userId);
        stay = repository.save(stay);

        // Update Room Status to DIRTY
        if (stay.getRoomId() != null) {
            roomRepository.findById(stay.getRoomId()).ifPresent(room -> {
                room.setStatus(RoomStatus.DIRTY);
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
    public StayDTO directCheckIn(Long guestId, Long roomId, Integer adults, Integer children, java.time.LocalDate dateOut, Long userId) {
        validateUser(userId);
        var room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));
        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new RuntimeException("Room " + room.getRoomNumber() + " is not available for check-in.");
        }

        // Validate no existing active stay for this guest
        if (repository.countByGuestIdAndStatusIn(guestId, List.of(StayStatus.ACTIVE, StayStatus.OVERSTAY)) > 0) {
            throw new RuntimeException("Guest already has an active stay.");
        }

        StayEntity stay = new StayEntity();
        stay.setReservationId(null);
        stay.setGuestId(guestId);
        stay.setRoomId(roomId);
        stay.setDateIn(LocalDateTime.now());
        stay.setDateOut(dateOut != null ? dateOut.atTime(12, 0) : null);
        stay.setAdults(adults != null ? adults : 1);
        stay.setChildren(children != null ? children : 0);
        stay.setStatus(StayStatus.ACTIVE);
        stay.setCheckedInBy(userId);

        stay = repository.save(stay);

        // Update Room Status to OCCUPIED
        room.setStatus(RoomStatus.OCCUPIED);
        roomRepository.save(room);

        // Create Folio for the Stay
        FolioDTO folio = folioService.createFolioForStay(stay.getId());

        // Auto-post Room Charge (Advance Billing)
        postRoomCharge(stay, folio.getId(), userId);

        return stayMapper.toDto(stay);
    }

    @Override
    @Transactional
    public StayDTO checkOutByReservationId(Long reservationId, Long userId) {
        validateUser(userId);
        StayEntity stay = repository.findByReservationIdAndStatusIn(reservationId, List.of(StayStatus.ACTIVE, StayStatus.DUE_CHECKOUT, StayStatus.OVERSTAY))
                .stream()
                .sorted((s1, s2) -> s2.getDateIn().compareTo(s1.getDateIn())) // Most recent first
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Active or Overstayed Stay not found for this reservation"));
        return checkOut(stay.getId(), userId);
    }

    private void postRoomCharge(StayEntity stay, Long folioId, Long userId) {
        long nights = 0;
        LocalDateTime periodStart = stay.getDateIn();
        LocalDateTime periodEnd = stay.getDateOut();
        
        if (periodStart != null && periodEnd != null) {
            nights = ChronoUnit.DAYS.between(periodStart.toLocalDate(), periodEnd.toLocalDate());
        }
        if (nights <= 0) {
            nights = 1; // Minimum 1 night charge
            if (periodStart != null) {
                // If 1 night, end date is next day 11 AM
                periodEnd = periodStart.toLocalDate().plusDays(1).atTime(12, 0);
            }
        }

        var room = roomRepository.findById(stay.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        BigDecimal rate = stay.getRatePerNight();
        if (rate == null && room.getRoomType() != null) {
            rate = room.getRoomType().getDefaultRate();
        }
        if (rate == null) {
            rate = BigDecimal.ZERO;
        }

        FolioChargeDTO chargeDto = FolioChargeDTO.builder()
                .folioId(folioId)
                .description("Room Charge - " + nights + " Night(s)")
                .quantity(new BigDecimal(nights))
                .unitPrice(rate)
                .taxPct(BigDecimal.ZERO)
                .discountPct(BigDecimal.ZERO)
                .periodStart(periodStart)
                .periodEnd(periodEnd)
                .addedBy(userId)
                .build();

        // Try to find or create "Room Charge" type
        chargeTypeRepository.findByName("Room Charge").ifPresent(type -> {
            chargeDto.setChargeTypeId(type.getId());
        });

        folioService.addCharge(folioId, chargeDto, userId);
    }

    @Override
    public List<StayDTO> findActiveStays() {
        return repository.findAllByStatusIn(List.of(StayStatus.ACTIVE, StayStatus.DUE_CHECKOUT, StayStatus.OVERSTAY))
                .stream()
                .map(stayMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Scheduled(cron = "0 0 * * * *") // Every hour
    public void autoFlagOverstays() {
        LocalDateTime now = LocalDateTime.now();
        java.util.List<com.hotel_erp.hotel_erp.modules.master.TenantConfigEntity> tenants = tenantConfigRepository.findAll();
        
        for (com.hotel_erp.hotel_erp.modules.master.TenantConfigEntity tenant : tenants) {
            try {
                com.hotel_erp.hotel_erp.config.tenant.TenantContext.setCurrentTenant(tenant.getDbKey());
                List<StayEntity> activeStays = repository.findAllByStatusIn(List.of(StayStatus.ACTIVE, StayStatus.DUE_CHECKOUT));
                for (StayEntity stay : activeStays) {
                    try {
                        stayFlagService.flagStayIfDue(stay.getId(), now);
                    } catch (Exception e) {
                        // Log and continue to next stay so one failure doesn't block the whole job
                        System.err.println("Failed to flag stay " + stay.getId() + " for tenant " + tenant.getDbKey() + ": " + e.getMessage());
                    }
                }
            } finally {
                com.hotel_erp.hotel_erp.config.tenant.TenantContext.clear();
            }
        }
    }

    @Override
    public List<StayDTO> findByGuestId(Long guestId) {
        return repository.findAllByGuestIdOrderByDateInDesc(guestId)
                .stream()
                .map(stayMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StayDTO extendStay(Long id, LocalDateTime newDateOut, Long userId) {
        validateUser(userId);
        StayEntity stay = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stay not found"));

        if (stay.getStatus() == StayStatus.CHECKED_OUT) {
            throw new RuntimeException("Cannot extend a checked-out stay");
        }

        // Normalize newDateOut to 11:00 AM
        LocalDateTime normalizedNewDateOut = newDateOut != null ? newDateOut.toLocalDate().atTime(12, 0) : null;
        LocalDateTime oldDateOut = stay.getDateOut();
        
        if (normalizedNewDateOut != null && oldDateOut != null && normalizedNewDateOut.toLocalDate().isBefore(oldDateOut.toLocalDate())) {
            throw new RuntimeException("New checkout date must be after current checkout date");
        }

        if (oldDateOut == null) {
            throw new RuntimeException("Cannot extend a stay with no original checkout date set.");
        }

        if (normalizedNewDateOut == null) {
            throw new RuntimeException("New checkout date must be provided");
        }
        
        long additionalNights = ChronoUnit.DAYS.between(oldDateOut.toLocalDate(), normalizedNewDateOut.toLocalDate());
        
        var room = roomRepository.findById(stay.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found for stay: " + id));

        // Re-acquire room if it was released during overstay period
        if (stay.getStatus() == StayStatus.OVERSTAY || stay.getStatus() == StayStatus.DUE_CHECKOUT) {
            boolean roomAssignedToAnother = repository.countByRoomIdAndStatus(stay.getRoomId(), StayStatus.ACTIVE) > 0;
            if (roomAssignedToAnother) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Room " + room.getRoomNumber() + " has been assigned to another guest.");
            }
            room.setStatus(RoomStatus.OCCUPIED);
            roomRepository.save(room);
        }

        if (additionalNights > 0) {
            var folio = folioRepository.findByStayId(id).orElseThrow(() -> new RuntimeException("Folio not found for stay: " + id));
            
            // DOUBLE-BILLING CHECK: Verify if room charges for this period already exist
            boolean alreadyBilled = folioChargeRepository.findAllByFolioId(folio.getId()).stream()
                    .filter(c -> !c.isVoided())
                    .filter(c -> c.getChargeType() != null && "Room Charge".equals(c.getChargeType().getName()))
                    .anyMatch(c -> c.getPeriodStart() != null && c.getPeriodStart().toLocalDate().isEqual(oldDateOut.toLocalDate()));

            if (!alreadyBilled) {
                BigDecimal rate = stay.getRatePerNight();
                if (rate == null && room.getRoomType() != null) {
                    rate = room.getRoomType().getDefaultRate();
                }
                if (rate == null) {
                    rate = BigDecimal.ZERO;
                }
                
                FolioChargeDTO chargeDto = FolioChargeDTO.builder()
                        .folioId(folio.getId())
                        .description("Room Charge Extension - " + additionalNights + " Night(s)")
                        .quantity(new BigDecimal(additionalNights))
                        .unitPrice(rate)
                        .taxPct(BigDecimal.ZERO)
                        .discountPct(BigDecimal.ZERO)
                        .periodStart(oldDateOut)
                        .periodEnd(normalizedNewDateOut)
                        .addedBy(userId)
                        .build();

                chargeTypeRepository.findByName("Room Charge").ifPresent(type -> chargeDto.setChargeTypeId(type.getId()));
                folioService.addCharge(folio.getId(), chargeDto, userId);
            }
        }

        stay.setDateOut(normalizedNewDateOut);
        if ((stay.getStatus() == StayStatus.OVERSTAY || stay.getStatus() == StayStatus.DUE_CHECKOUT) 
                && normalizedNewDateOut.isAfter(LocalDateTime.now())) {
            stay.setStatus(StayStatus.ACTIVE);
        }

        return stayMapper.toDto(repository.save(stay));
    }

    @Override
    @Transactional
    public void voidStay(Long stayId, Long userId) {
        validateUser(userId);
        StayEntity stay = repository.findById(stayId)
                .orElseThrow(() -> new RuntimeException("Stay not found"));

        if (stay.getStatus() == StayStatus.CHECKED_OUT) {
            throw new RuntimeException("Cannot void a checked-out stay");
        }

        stay.setStatus(StayStatus.VOIDED);
        repository.save(stay);

        // Revert Room Status to AVAILABLE
        if (stay.getRoomId() != null) {
            roomRepository.findById(stay.getRoomId()).ifPresent(room -> {
                room.setStatus(RoomStatus.AVAILABLE);
                roomRepository.save(room);
            });
        }

        // Revert Reservation Status if linked and currently CHECKED_IN
        if (stay.getReservationId() != null) {
            reservationRepository.findById(stay.getReservationId()).ifPresent(res -> {
                if (res.getStatus() == ReservationStatus.CHECKED_IN) {
                    res.setStatus(ReservationStatus.BOOKED);
                    reservationRepository.save(res);
                }
            });
        }

        folioRepository.findByStayId(stayId).ifPresent(folio -> {
            if (folio.getStatus() == com.hotel_erp.hotel_erp.modules.folio.FolioStatus.OPEN) {
                folioService.voidFolio(folio.getId(), userId);
            }
        });
    }

    @Override
    @Transactional
    public void markNoShow(Long reservationId, Long userId) {
        validateUser(userId);
        reservationRepository.findById(reservationId).ifPresent(res -> {
            res.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(res);
            
            roomRepository.findById(res.getRoomId()).ifPresent(room -> {
                if (room.getStatus() == RoomStatus.OCCUPIED) {
                    room.setStatus(RoomStatus.AVAILABLE);
                    roomRepository.save(room);
                }
            });

            // Void reservation folio
            folioRepository.findByReservationId(reservationId).ifPresent(folio -> {
                folioService.voidFolio(folio.getId(), userId);
            });
        });
    }
}

