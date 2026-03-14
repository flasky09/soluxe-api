package com.hotel_erp.hotel_erp.modules.stay;

import com.hotel_erp.hotel_erp.modules.folio.FolioEntity;
import com.hotel_erp.hotel_erp.modules.folio.FolioRepository;
import com.hotel_erp.hotel_erp.modules.folio.FolioStatus;
import com.hotel_erp.hotel_erp.modules.reservation.ReservationEntity;
import com.hotel_erp.hotel_erp.modules.reservation.ReservationRepository;
import com.hotel_erp.hotel_erp.modules.reservation.ReservationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class StayIntegrationTest {

    @Autowired
    private StayService stayService;

    @Autowired
    private com.hotel_erp.hotel_erp.modules.folio.FolioService folioService;

    @Autowired
    private StayRepository stayRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private FolioRepository folioRepository;

    private ReservationEntity testReservation;

    @BeforeEach
    void setUp() {
        // Create a test reservation
        testReservation = new ReservationEntity();
        testReservation.setGuestId(1L);
        testReservation.setRoomTypeId(1L);
        testReservation.setDateIn(LocalDate.now());
        testReservation.setDateOut(LocalDate.now().plusDays(2));
        testReservation.setAdults(2);
        testReservation.setStatus(ReservationStatus.BOOKED);
        testReservation = reservationRepository.save(testReservation);
    }

    @Test
    void testCheckInFlow() {
        Long roomId = 1L;
        Long userId = 1L;

        StayDTO stayDto = stayService.checkIn(testReservation.getId(), roomId, userId);

        assertNotNull(stayDto);
        assertEquals(StayStatus.ACTIVE, stayDto.getStatus());
        assertEquals(roomId, stayDto.getRoomId());
        
        // Verify Reservation Status
        ReservationEntity updatedReservation = reservationRepository.findById(testReservation.getId()).get();
        assertEquals(ReservationStatus.CHECKED_IN, updatedReservation.getStatus());

        // Verify Folio was created
        FolioEntity createdFolio = folioRepository.findByStayId(stayDto.getId()).get();
        assertNotNull(createdFolio);
        assertEquals(FolioStatus.OPEN, createdFolio.getStatus());
        // Since Advance Billing is active, totalAmount should NOT be 0.
        // It should be 10000.00 (2 nights * 5000.00 default rate)
        assertEquals(10000, createdFolio.getTotalAmount().intValue());
    }

    @Test
    void testCheckOutFlow() {
        Long roomId = 1L;
        Long userId = 1L;

        // Perform Check-In First
        StayDTO stayDto = stayService.checkIn(testReservation.getId(), roomId, userId);
        
        // Add Payment to settle folio (Advance Billing posts 10000.00)
        com.hotel_erp.hotel_erp.modules.folio.FolioPaymentDTO paymentDto = com.hotel_erp.hotel_erp.modules.folio.FolioPaymentDTO.builder()
                .amount(java.math.BigDecimal.valueOf(10000))
                .folioId(folioRepository.findByStayId(stayDto.getId()).get().getId())
                .recordedBy(userId)
                .build();
        folioService.addPayment(paymentDto.getFolioId(), paymentDto, userId);

        // Perform Check-Out
        StayDTO checkOutDto = stayService.checkOut(stayDto.getId(), userId);
        
        assertEquals(StayStatus.CHECKED_OUT, checkOutDto.getStatus());
        assertNotNull(checkOutDto.getActualDateOut());
        
        // Verify Folio was closed
        FolioEntity closedFolio = folioRepository.findByStayId(stayDto.getId()).get();
        assertEquals(FolioStatus.CLOSED, closedFolio.getStatus());
        assertNotNull(closedFolio.getClosedAt());
    }
}
