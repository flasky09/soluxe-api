package com.hotel_erp.hotel_erp.modules.dashboard;

import com.hotel_erp.hotel_erp.modules.reservation.ReservationRepository;
import com.hotel_erp.hotel_erp.modules.reservation.ReservationStatus;
import com.hotel_erp.hotel_erp.modules.room.RoomRepository;
import com.hotel_erp.hotel_erp.modules.stay.StayRepository;
import com.hotel_erp.hotel_erp.modules.stay.StayStatus;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ReservationRepository reservationRepository;
    private final StayRepository stayRepository;
    private final RoomRepository roomRepository;

    public DashboardSummaryDTO getSummary() {
        LocalDate today = LocalDate.now();

        // Tenant isolation is handled automatically by Hibernate filters
        long arrivals = reservationRepository.countByStatusAndDateIn(ReservationStatus.BOOKED, today);
        long departures = reservationRepository.countByStatusAndDateOut(ReservationStatus.CHECKED_IN, today);
        
        long activeStays = stayRepository.findAllByStatus(StayStatus.ACTIVE).size();

        long totalRooms = roomRepository.count();
        double occupancyRate = totalRooms > 0 ? (double) activeStays / totalRooms * 100 : 0.0;

        return DashboardSummaryDTO.builder()
                .totalArrivalsToday(arrivals)
                .totalDeparturesToday(departures)
                .activeStays(activeStays)
                .occupancyRate(Math.round(occupancyRate * 100.0) / 100.0)
                .build();
    }
}
