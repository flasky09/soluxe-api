package com.hotel_erp.hotel_erp.modules.stay;

import com.hotel_erp.hotel_erp.modules.reservation.ReservationEntity;
import com.hotel_erp.hotel_erp.modules.reservation.ReservationRepository;
import com.hotel_erp.hotel_erp.modules.reservation.ReservationStatus;
import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class StayServiceImpl extends BaseServiceImpl<StayEntity, Long, StayRepository> implements StayService {

    private final ReservationRepository reservationRepository;
    private final StayMapper stayMapper;
    private final com.hotel_erp.hotel_erp.modules.folio.FolioService folioService;
    private final com.hotel_erp.hotel_erp.modules.folio.FolioRepository folioRepository;

    public StayServiceImpl(StayRepository repository,
                           ReservationRepository reservationRepository,
                           StayMapper stayMapper,
                           com.hotel_erp.hotel_erp.modules.folio.FolioService folioService,
                           com.hotel_erp.hotel_erp.modules.folio.FolioRepository folioRepository) {
        super(repository);
        this.reservationRepository = reservationRepository;
        this.stayMapper = stayMapper;
        this.folioService = folioService;
        this.folioRepository = folioRepository;
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
        stay.setStatus(StayStatus.ACTIVE);
        stay.setCheckedInBy(userId);
        
        stay = repository.save(stay);
        
        reservation.setStatus(ReservationStatus.CHECKED_IN);
        reservationRepository.save(reservation);

        // Create Folio for the Stay
        folioService.createFolioForStay(stay.getId());

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

        stay.setStatus(StayStatus.CHECKED_OUT);
        stay.setActualDateOut(LocalDateTime.now());
        stay.setCheckedOutBy(userId);

        stay = repository.save(stay);

        // Close Folio
        folioRepository.findByStayId(stayId).ifPresent(folio -> {
            folio.setStatus(com.hotel_erp.hotel_erp.modules.folio.FolioStatus.CLOSED);
            folio.setClosedAt(LocalDateTime.now());
            folioRepository.save(folio);
        });

        return stayMapper.toDto(stay);
    }
}
