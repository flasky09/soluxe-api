package com.hotel_erp.hotel_erp.modules.dashboard;

import com.hotel_erp.hotel_erp.modules.employee.leave.LeaveRequestRepository;
import com.hotel_erp.hotel_erp.modules.folio.FolioChargeRepository;
import com.hotel_erp.hotel_erp.modules.inventory.InventoryItemRepository;
import com.hotel_erp.hotel_erp.modules.inventory.purchaseorder.PurchaseOrderRepository;
import com.hotel_erp.hotel_erp.modules.reservation.ReservationRepository;
import com.hotel_erp.hotel_erp.modules.reservation.ReservationStatus;
import com.hotel_erp.hotel_erp.modules.room.RoomRepository;
import com.hotel_erp.hotel_erp.modules.stay.StayRepository;
import com.hotel_erp.hotel_erp.modules.stay.StayStatus;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ReservationRepository reservationRepository;
    private final StayRepository stayRepository;
    private final RoomRepository roomRepository;
    private final FolioChargeRepository folioChargeRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public DashboardSummaryDTO getSummary() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(java.time.LocalTime.MAX);

        long arrivals = reservationRepository.countByStatusAndDateInLessThanEqual(ReservationStatus.BOOKED, today);
        long departures = reservationRepository.countByStatusAndDateOut(ReservationStatus.CHECKED_IN, today);
        long activeStays = stayRepository.countByStatusIn(java.util.List.of(StayStatus.ACTIVE, StayStatus.OVERSTAY));

        long totalRooms = roomRepository.count();
        double occupancyRate = totalRooms > 0 ? (double) activeStays / totalRooms * 100 : 0.0;

        // Financials
        java.util.List<com.hotel_erp.hotel_erp.modules.folio.FolioChargeEntity> todayCharges = 
            folioChargeRepository.findAllByDateRange(startOfDay, endOfDay);
        
        double dailyRevenue = todayCharges.stream()
                .mapToDouble(c -> c.getTotalAmount().doubleValue())
                .sum();
        
        double dailyRoomRevenue = todayCharges.stream()
                .filter(c -> c.getChargeType() != null && "Room".equalsIgnoreCase(c.getChargeType().getName()))
                .mapToDouble(c -> c.getTotalAmount().doubleValue())
                .sum();

        double adr = activeStays > 0 ? dailyRoomRevenue / activeStays : 0.0;
        double revpar = totalRooms > 0 ? dailyRoomRevenue / totalRooms : 0.0;

        // Operational Alerts
        long dirtyRooms = roomRepository.findAll().stream()
                .filter(r -> r.getStatus() == com.hotel_erp.hotel_erp.modules.room.RoomStatus.DIRTY)
                .count();

        long pendingLeaves = leaveRequestRepository.findAll().stream()
                .filter(l -> l.getStatus() == com.hotel_erp.hotel_erp.modules.employee.leave.LeaveStatus.PENDING)
                .count();

        long pendingPOs = purchaseOrderRepository.findAll().stream()
                .filter(p -> "PENDING".equalsIgnoreCase(p.getStatus().name()))
                .count();

        long lowStockItems = inventoryItemRepository.findAll().stream()
                .filter(item -> item.getCurrentStock() != null && item.getMinimumStock() != null 
                        && item.getCurrentStock().compareTo(item.getMinimumStock()) <= 0)
                .count();

        return DashboardSummaryDTO.builder()
                .totalArrivalsToday(arrivals)
                .totalDeparturesToday(departures)
                .activeStays(activeStays)
                .occupancyRate(Math.round(occupancyRate * 100.0) / 100.0)
                .dailyRevenue(Math.round(dailyRevenue * 100.0) / 100.0)
                .averageDailyRate(Math.round(adr * 100.0) / 100.0)
                .revenuePerAvailableRoom(Math.round(revpar * 100.0) / 100.0)
                .pendingHousekeeping(dirtyRooms)
                .pendingLeaveRequests(pendingLeaves)
                .pendingPurchaseOrders(pendingPOs)
                .lowStockItems(lowStockItems)
                .build();
    }
}
