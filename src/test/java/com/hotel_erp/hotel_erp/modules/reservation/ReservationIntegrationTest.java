package com.hotel_erp.hotel_erp.modules.reservation;

import com.hotel_erp.hotel_erp.modules.guest.GuestEntity;
import com.hotel_erp.hotel_erp.modules.guest.GuestRepository;
import com.hotel_erp.hotel_erp.modules.room.RoomTypeEntity;
import com.hotel_erp.hotel_erp.modules.room.RoomTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ReservationIntegrationTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private GuestRepository guestRepository;

    private GuestEntity testGuest;
    private RoomTypeEntity testRoomType;

    @BeforeEach
    void setUp() {
        // Create Room Type
        testRoomType = new RoomTypeEntity();
        testRoomType.setName("Deluxe " + System.currentTimeMillis());
        testRoomType.setDefaultRate(new BigDecimal("7500"));
        testRoomType = roomTypeRepository.save(testRoomType);

        // Create Guest
        testGuest = new GuestEntity();
        // Create Guest
        testGuest = new GuestEntity();
        testGuest.setFullName("John Doe");
        testGuest.setEmail("john.doe@test.com");
        testGuest.setPhone("+1234567890");
        testGuest = guestRepository.save(testGuest);
    }

    @Test
    void testCreateReservation() {
        ReservationDTO dto = new ReservationDTO();
        dto.setGuestId(testGuest.getId());
        dto.setRoomTypeId(testRoomType.getId());
        dto.setDateIn(LocalDate.now().plusDays(1));
        dto.setDateOut(LocalDate.now().plusDays(3));
        dto.setAdults(2);
        dto.setStatus(ReservationStatus.BOOKED.name());

        ReservationDTO created = reservationService.createFromDto(dto);

        assertNotNull(created.getId());
        assertEquals(testGuest.getId(), created.getGuestId());
        assertEquals(ReservationStatus.BOOKED.name(), created.getStatus());
    }

    @Test
    void testGetTodayArrivalsIncludesPastDue() {
        // 1. Create a reservation that is past-due (dateIn was yesterday, but still BOOKED)
        ReservationEntity pastDue = new ReservationEntity();
        pastDue.setGuestId(testGuest.getId());
        pastDue.setRoomTypeId(testRoomType.getId());
        pastDue.setDateIn(LocalDate.now().minusDays(2)); // Should have arrived 2 days ago
        pastDue.setDateOut(LocalDate.now().plusDays(1));
        pastDue.setAdults(1);
        pastDue.setStatus(ReservationStatus.BOOKED);
        final Long pastDueId = reservationRepository.save(pastDue).getId();

        // 2. Create a reservation arriving exactly today
        ReservationEntity today = new ReservationEntity();
        today.setGuestId(testGuest.getId());
        today.setRoomTypeId(testRoomType.getId());
        today.setDateIn(LocalDate.now());
        today.setDateOut(LocalDate.now().plusDays(3));
        today.setAdults(1);
        today.setStatus(ReservationStatus.BOOKED);
        final Long todayId = reservationRepository.save(today).getId();

        // 3. Create a reservation arriving tomorrow
        ReservationEntity tomorrow = new ReservationEntity();
        tomorrow.setGuestId(testGuest.getId());
        tomorrow.setRoomTypeId(testRoomType.getId());
        tomorrow.setDateIn(LocalDate.now().plusDays(1));
        tomorrow.setDateOut(LocalDate.now().plusDays(4));
        tomorrow.setAdults(1);
        tomorrow.setStatus(ReservationStatus.BOOKED);
        final Long tomorrowId = reservationRepository.save(tomorrow).getId();

        // Execute
        List<ReservationDTO> arrivals = reservationService.getTodayArrivals();

        // Verify
        // Arrivals should contain BOTH pastDue and today, but NOT tomorrow
        boolean containsPastDue = arrivals.stream().anyMatch(r -> r.getId().equals(pastDueId));
        boolean containsToday = arrivals.stream().anyMatch(r -> r.getId().equals(todayId));
        boolean containsTomorrow = arrivals.stream().anyMatch(r -> r.getId().equals(tomorrowId));

        assertTrue(containsPastDue, "Past due BOOKED reservations should be included in Pending Arrivals");
        assertTrue(containsToday, "Today's BOOKED reservations should be included in Pending Arrivals");
        assertFalse(containsTomorrow, "Future reservations should NOT be in Pending Arrivals");
    }
}
