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

import com.hotel_erp.hotel_erp.modules.room.RoomEntity;
import com.hotel_erp.hotel_erp.modules.room.RoomTypeEntity;
import com.hotel_erp.hotel_erp.modules.room.RoomRepository;
import com.hotel_erp.hotel_erp.modules.room.RoomTypeRepository;
import com.hotel_erp.hotel_erp.modules.room.RoomStatus;
import com.hotel_erp.hotel_erp.modules.folio.ChargeTypeEntity;
import com.hotel_erp.hotel_erp.modules.folio.ChargeTypeRepository;
import com.hotel_erp.hotel_erp.modules.folio.FolioDTO;
import com.hotel_erp.hotel_erp.modules.folio.FolioPaymentDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
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

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private ChargeTypeRepository chargeTypeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private ReservationEntity testReservation;
    private Long testRoomId;

    @BeforeEach
    void setUp() {
        // Create Room Type
        RoomTypeEntity rt = new RoomTypeEntity();
        rt.setName("Standard " + System.currentTimeMillis());
        rt.setDefaultRate(new BigDecimal("5000"));
        rt = roomTypeRepository.save(rt);

        // Create Room
        RoomEntity room = new RoomEntity();
        room.setRoomNumber("ROOM-" + System.currentTimeMillis());
        room.setRoomType(rt);
        room.setStatus(RoomStatus.AVAILABLE);
        room = roomRepository.save(room);
        testRoomId = room.getId();

        // Create Room Charge type
        ChargeTypeEntity ct = new ChargeTypeEntity();
        ct.setName("Room Charge");
        if (chargeTypeRepository.findByName("Room Charge").isEmpty()) {
            chargeTypeRepository.save(ct);
        }

        // Create a test reservation
        testReservation = new ReservationEntity();
        testReservation.setGuestId(1L);
        testReservation.setRoomTypeId(rt.getId());
        testReservation.setDateIn(LocalDate.now());
        testReservation.setDateOut(LocalDate.now().plusDays(2));
        testReservation.setAdults(2);
        testReservation.setStatus(ReservationStatus.BOOKED);
        testReservation = reservationRepository.save(testReservation);
    }

    @Test
    void testCheckInFlow() {
        Long userId = 1L;

        StayDTO stayDto = stayService.checkIn(testReservation.getId(), testRoomId, userId);

        assertNotNull(stayDto);
        assertEquals(StayStatus.ACTIVE, stayDto.getStatus());
        assertEquals(testRoomId, stayDto.getRoomId());
        
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
        Long userId = 1L;

        // Perform Check-In First
        StayDTO stayDto = stayService.checkIn(testReservation.getId(), testRoomId, userId);
        
        // Add Payment to settle folio (Advance Billing posts 10000.00)
        // We pay in full upfront; checkout will post an early-departure credit if applicable.
        FolioPaymentDTO paymentDto = FolioPaymentDTO.builder()
                .amount(BigDecimal.valueOf(10000))
                .folioId(folioRepository.findByStayId(stayDto.getId()).get().getId())
                .recordedBy(userId)
                .build();
        folioService.addPayment(paymentDto.getFolioId(), paymentDto, userId);

        // Issue a refund of 5000 so that when checkout applies the -5000 early-departure
        // credit charge the folio balance will be exactly 0.
        // Folio state before checkout: charges=10000, payments=10000-5000=5000 → balance=+5000.
        // checkOut posts credit charge of -5000 → charges total=5000, payments=5000 → balance=0.
        FolioPaymentDTO refundDto = FolioPaymentDTO.builder()
                .amount(new BigDecimal("-5000"))
                .folioId(paymentDto.getFolioId())
                .recordedBy(userId)
                .build();
        folioService.addPayment(refundDto.getFolioId(), refundDto, userId);

        // Perform Check-Out
        StayDTO checkOutDto = stayService.checkOut(stayDto.getId(), userId);
        
        assertEquals(StayStatus.CHECKED_OUT, checkOutDto.getStatus());
        assertNotNull(checkOutDto.getActualDateOut());
        
        // Verify Folio was closed
        FolioEntity closedFolio = folioRepository.findByStayId(stayDto.getId()).get();
        assertEquals(FolioStatus.CLOSED, closedFolio.getStatus());
        assertNotNull(closedFolio.getClosedAt());
    }

    @Test
    void testOverstayAndDueCheckoutLogic() {
        Long userId = 1L;

        // 1. Check-in today
        StayDTO stayDto = stayService.checkIn(testReservation.getId(), testRoomId, userId);
        
        // Verify default dateOut is at 12:00 (noon) — set by StayServiceImpl
        LocalDateTime expectedDateOut = testReservation.getDateOut().atTime(12, 0);
        assertEquals(expectedDateOut, stayDto.getDateOut());
        assertEquals(StayStatus.ACTIVE, stayDto.getStatus());

        // 2. Simulate departure day (set dateOut to late tonight to prevent test flakiness if run past 11am)
        StayEntity stay = stayRepository.findById(stayDto.getId()).get();
        stay.setDateOut(LocalDate.now().atTime(23, 59));
        stayRepository.save(stay);
        entityManager.flush(); // Ensure REQUIRES_NEW sub-transaction in StayFlagService sees the change

        // Run autoFlagOverstays - should become DUE_CHECKOUT because dateOut is today
        stayService.autoFlagOverstays();
        
        StayEntity updatedStay = stayRepository.findById(stayDto.getId()).get();
        assertEquals(StayStatus.DUE_CHECKOUT, updatedStay.getStatus());

        // 3. Simulate a past departure date (yesterday) — OVERSTAY triggers when dateOut is before today,
        //    regardless of current hour of day. Using 'now - 1h' fails before 11am.
        stay = stayRepository.findById(stayDto.getId()).get();
        stay.setDateOut(LocalDate.now().minusDays(1).atTime(23, 59));
        stayRepository.save(stay);
        entityManager.flush();

        stayService.autoFlagOverstays();

        StayEntity overstayedStay = stayRepository.findById(stayDto.getId()).get();
        assertEquals(StayStatus.OVERSTAY, overstayedStay.getStatus());
    }

    @Test
    void testEarlyCheckOutWithRefundFlow() {
        Long userId = 1L;

        // 1. Check-in for 2 nights (10000 limit initially billed)
        StayDTO stayDto = stayService.checkIn(testReservation.getId(), testRoomId, userId);
        
        // 2. Guest pays in full in advance
        FolioPaymentDTO paymentDto = FolioPaymentDTO.builder()
                .amount(BigDecimal.valueOf(10000))
                .folioId(folioRepository.findByStayId(stayDto.getId()).get().getId())
                .recordedBy(userId)
                .build();
        folioService.addPayment(paymentDto.getFolioId(), paymentDto, userId);

        // 3. Guest checks out early (today) instead of after 2 nights
        // actualNights = max(1, today - today) = 1 night.
        // plannedNights = 2 nights (based on reservation dateOut).
        // Difference = 1 night (Credit of 1 * 5000 = 5000).
        // Planned nights = 2 (today to today+2).
        // Since we are mocking "early checkout", let's just let checkOut calculate the nights based on now.
        // Wait, checkOut uses LocalDate.now() automatically for ACTUAL nights.
        // Since testReservation was made for 2 nights starting today, and today is today:
        // actualNights = max(1, today - today) = 1 night.
        // plannedNights = 2 nights.
        // Difference = 1 night (Credit of 1 * 5000 = 5000).
        
        // So checking out now will apply a credit of 5000 automatically.
        // Wait, checkOut method throws if balance is not 0.
        // Wait! In the real app, we said "Final balance must be $0" which means we issue refund FIRST.
        // Let's issue the refund first, then checkout.
        // But how do we issue refund if the adjustment hasn't been posted yet?
        // Wait, in real app, we calculate the adjustment mathematically on UI, post refund, then checkout.
        // In backend, checkOut() applies the adjustment and then checks balance!
        
        // So if we refund 5000 before checkout:
        // current folio = 10000 charges - 10000 payments = 0 balance.
        // Refund 5000 -> 10000 charges - (-5000) payments? No, Payment is +10000, Refund is -5000. Total Payments = 5000.
        // Folio Balance = 10000 - 5000 = +5000.
        
        FolioPaymentDTO refundDto = FolioPaymentDTO.builder()
                .amount(new BigDecimal("-5000"))
                .folioId(paymentDto.getFolioId())
                .recordedBy(userId)
                .build();
        folioService.addPayment(refundDto.getFolioId(), refundDto, userId);

        // Folio balance is now +5000.
        // Check-out should apply credit of -5000.
        // Final balance = 0, checkOut should succeed!
        StayDTO checkOutDto = stayService.checkOut(stayDto.getId(), userId);
        
        assertEquals(StayStatus.CHECKED_OUT, checkOutDto.getStatus());
        FolioEntity closedFolio = folioRepository.findByStayId(stayDto.getId()).get();
        assertEquals(FolioStatus.CLOSED, closedFolio.getStatus());
        // Verify balance is perfectly 0
        assertEquals(0, BigDecimal.ZERO.compareTo(closedFolio.getTotalAmount()));
    }

    @Test
    void testCheckOutWithMultipleCharges() {
        Long userId = 1L;

        // 1. Check-in
        StayDTO stayDto = stayService.checkIn(testReservation.getId(), testRoomId, userId);
        FolioEntity folio = folioRepository.findByStayId(stayDto.getId()).get();
        
        // At this point, auto room charge is 10000 (2 nights * 5000)
        // 2. Add Food and Laundry charge types
        ChargeTypeEntity foodType = new ChargeTypeEntity();
        foodType.setName("Food & Beverage");
        chargeTypeRepository.save(foodType);

        ChargeTypeEntity laundryType = new ChargeTypeEntity();
        laundryType.setName("Laundry");
        chargeTypeRepository.save(laundryType);

        // 3. Add charges
        com.hotel_erp.hotel_erp.modules.folio.FolioChargeDTO foodCharge = com.hotel_erp.hotel_erp.modules.folio.FolioChargeDTO.builder()
                .folioId(folio.getId())
                .description("Restaurant Dinner")
                .chargeTypeId(foodType.getId())
                .quantity(BigDecimal.ONE)
                .unitPrice(new BigDecimal("1500")) // +1500
                .build();
        folioService.addCharge(folio.getId(), foodCharge, userId);

        com.hotel_erp.hotel_erp.modules.folio.FolioChargeDTO laundryCharge = com.hotel_erp.hotel_erp.modules.folio.FolioChargeDTO.builder()
                .folioId(folio.getId())
                .description("Dry Cleaning")
                .chargeTypeId(laundryType.getId())
                .quantity(new BigDecimal("2"))
                .unitPrice(new BigDecimal("250")) // +500
                .build();
        folioService.addCharge(folio.getId(), laundryCharge, userId);

        // Total Expected Charges: 10000 (Room) + 1500 (Food) + 500 (Laundry) = 12000
        
        // 4. Guest pays only 10000 (leaving 2000 unpaid before adjustment)
        FolioPaymentDTO partialPayment = FolioPaymentDTO.builder()
                .amount(new BigDecimal("10000"))
                .folioId(folio.getId())
                .recordedBy(userId)
                .build();
        folioService.addPayment(folio.getId(), partialPayment, userId);

        // 5. Guest needs a refund of 3000 to reach 0 balance (after 5000 adjustment)
        FolioPaymentDTO refundPayment = FolioPaymentDTO.builder()
                .amount(new BigDecimal("-3000")) // Refund
                .folioId(folio.getId())
                .recordedBy(userId)
                .build();
        folioService.addPayment(folio.getId(), refundPayment, userId);

        // 6. Check-Out should now succeed
        StayDTO checkOutDto = stayService.checkOut(stayDto.getId(), userId);
        assertEquals(StayStatus.CHECKED_OUT, checkOutDto.getStatus());
    }

    @Test
    void testCheckOutFailsWithUnpaidBalance() {
        Long userId = 1L;

        StayDTO stayDto = stayService.checkIn(testReservation.getId(), testRoomId, userId);
        
        // We do not pay the 10000 room charge.
        // When checking out early, it posts -5000 credit.
        // Balance will be 10000 - 5000 = 5000 > 0.
        org.springframework.web.server.ResponseStatusException ex1 = assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            stayService.checkOut(stayDto.getId(), userId);
        });
        assertTrue(ex1.getMessage().contains("Outstanding folio balance"));
    }
}
