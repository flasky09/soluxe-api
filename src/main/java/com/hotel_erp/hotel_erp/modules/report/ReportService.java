package com.hotel_erp.hotel_erp.modules.report;

import com.hotel_erp.hotel_erp.modules.folio.FolioChargeEntity;
import com.hotel_erp.hotel_erp.modules.folio.FolioChargeRepository;
import com.hotel_erp.hotel_erp.modules.folio.FolioPaymentEntity;
import com.hotel_erp.hotel_erp.modules.folio.FolioPaymentRepository;
import com.hotel_erp.hotel_erp.modules.folio.FolioRepository;
import com.hotel_erp.hotel_erp.modules.expense.ExpenseEntity;
import com.hotel_erp.hotel_erp.modules.expense.ExpenseRepository;
import com.hotel_erp.hotel_erp.modules.inventory.purchaseorder.PurchaseOrderItemRepository;
import com.hotel_erp.hotel_erp.modules.inventory.purchaseorder.PurchaseOrderEntity;
import com.hotel_erp.hotel_erp.modules.inventory.purchaseorder.PurchaseOrderRepository;
import com.hotel_erp.hotel_erp.modules.inventory.InventoryItemRepository;
import com.hotel_erp.hotel_erp.modules.employee.EmployeeRepository;
import com.hotel_erp.hotel_erp.modules.finance.CashMovementRepository;
import com.hotel_erp.hotel_erp.modules.finance.CashMovementEntity;
import com.hotel_erp.hotel_erp.modules.finance.PettyCashRepository;
import com.hotel_erp.hotel_erp.modules.stay.StayRepository;
import com.hotel_erp.hotel_erp.modules.user.UserRepository;
import com.hotel_erp.hotel_erp.modules.user.UserEntity;
import com.hotel_erp.hotel_erp.modules.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final FolioChargeRepository folioChargeRepository;
    private final FolioPaymentRepository folioPaymentRepository;
    private final ExpenseRepository expenseRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final EmployeeRepository employeeRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final CashMovementRepository cashMovementRepository;
    private final PettyCashRepository pettyCashRepository;
    private final FolioRepository folioRepository;
    private final StayRepository stayRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public DailyRevenueDTO getDailyRevenue(LocalDate date) {
        // Fetch all charges for the given date
        List<FolioChargeEntity> dailyCharges = folioChargeRepository.findAllByDateRange(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );

        List<FolioPaymentEntity> payments = folioPaymentRepository.findAllByDateRange(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );

        List<ExpenseEntity> expenses = expenseRepository.findAllByExpenseDateBetween(date, date);
        BigDecimal totalExpenses = BigDecimal.ZERO;
        BigDecimal operationalExpenses = BigDecimal.ZERO;
        BigDecimal totalAssets = BigDecimal.ZERO;
        BigDecimal maintenanceCosts = BigDecimal.ZERO;

        for (ExpenseEntity e : expenses) {
            BigDecimal amt = e.getAmount() != null ? e.getAmount() : BigDecimal.ZERO;
            totalExpenses = totalExpenses.add(amt);
            
            boolean isAsset = e.getExpenseType() != null && e.getExpenseType().isAsset();
            if (isAsset) {
                totalAssets = totalAssets.add(amt);
            } else {
                operationalExpenses = operationalExpenses.add(amt);
                if (e.getExpenseType() != null && e.getExpenseType().getName() != null && 
                    e.getExpenseType().getName().toLowerCase().contains("maintenance")) {
                    maintenanceCosts = maintenanceCosts.add(amt);
                }
            }
        }
        
        BigDecimal supplyCosts = purchaseOrderItemRepository.getTotalSpendInDateRange(date, date);
        if (supplyCosts == null) {
            supplyCosts = BigDecimal.ZERO;
        }

        BigDecimal payrollExpenses = employeeRepository.getTotalPayroll();
        if (payrollExpenses == null) {
            payrollExpenses = BigDecimal.ZERO;
        }

        BigDecimal accountsReceivable = folioRepository.getOutstandingBalanceForClosedFolios();
        if (accountsReceivable == null) {
            accountsReceivable = BigDecimal.ZERO;
        }

        BigDecimal accountsPayable = purchaseOrderRepository.getTotalAccountsPayable();
        if (accountsPayable == null) {
            accountsPayable = BigDecimal.ZERO;
        }

        BigDecimal pettyCash = pettyCashRepository.getTotalPettyCashInDateRange(date, date);
        if (pettyCash == null) {
            pettyCash = BigDecimal.ZERO;
        }

        return calculateDailyRevenue(date, dailyCharges, payments, expenses,
                totalExpenses, operationalExpenses, totalAssets, maintenanceCosts,
                supplyCosts, payrollExpenses, accountsReceivable, accountsPayable, pettyCash);
    }

    public RevenueReportDTO getRevenueReport(LocalDate startDate, LocalDate endDate) {
        // Fetch all charges for the given date range
        List<FolioChargeEntity> dailyCharges = folioChargeRepository.findAllByDateRange(
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay()
        );

        List<FolioPaymentEntity> payments = folioPaymentRepository.findAllByDateRange(
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay()
        );

        BigDecimal totalPayments = payments.stream()
                .map(FolioPaymentEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<ExpenseEntity> expenses = expenseRepository.findAllByExpenseDateBetween(startDate, endDate);
        BigDecimal totalExpenses = BigDecimal.ZERO;
        BigDecimal operationalExpenses = BigDecimal.ZERO;
        BigDecimal totalAssets = BigDecimal.ZERO;
        BigDecimal maintenanceCosts = BigDecimal.ZERO;

        for (ExpenseEntity e : expenses) {
            BigDecimal amt = e.getAmount() != null ? e.getAmount() : BigDecimal.ZERO;
            totalExpenses = totalExpenses.add(amt);
            
            boolean isAsset = e.getExpenseType() != null && e.getExpenseType().isAsset();
            if (isAsset) {
                totalAssets = totalAssets.add(amt);
            } else {
                operationalExpenses = operationalExpenses.add(amt);
                if (e.getExpenseType() != null && e.getExpenseType().getName() != null && 
                    e.getExpenseType().getName().toLowerCase().contains("maintenance")) {
                    maintenanceCosts = maintenanceCosts.add(amt);
                }
            }
        }

        BigDecimal supplyCosts = purchaseOrderItemRepository.getTotalSpendInDateRange(startDate, endDate);
        if (supplyCosts == null) {
            supplyCosts = BigDecimal.ZERO;
        }

        BigDecimal payrollExpenses = employeeRepository.getTotalPayroll();
        if (payrollExpenses == null) {
            payrollExpenses = BigDecimal.ZERO;
        }

        BigDecimal accountsReceivable = folioRepository.getOutstandingBalanceForClosedFolios();
        if (accountsReceivable == null) {
            accountsReceivable = BigDecimal.ZERO;
        }

        BigDecimal accountsPayable = purchaseOrderRepository.getTotalAccountsPayable();
        if (accountsPayable == null) {
            accountsPayable = BigDecimal.ZERO;
        }

        BigDecimal pettyCash = pettyCashRepository.getTotalPettyCashInDateRange(startDate, endDate);
        if (pettyCash == null) {
            pettyCash = BigDecimal.ZERO;
        }

        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal netRevenue = BigDecimal.ZERO;
        BigDecimal taxCollected = BigDecimal.ZERO;

        for (FolioChargeEntity charge : dailyCharges) {
            BigDecimal total = charge.getTotalAmount();
            BigDecimal taxPct = charge.getTaxPct();
            
            BigDecimal net;
            if (taxPct != null && taxPct.compareTo(BigDecimal.ZERO) > 0) {
                // Net = Total / (1 + TaxPct/100)
                BigDecimal taxFactor = BigDecimal.ONE.add(taxPct.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
                net = total.divide(taxFactor, 2, RoundingMode.HALF_UP);
            } else {
                net = total;
            }

            totalRevenue = totalRevenue.add(total);
            netRevenue = netRevenue.add(net);
            taxCollected = taxCollected.add(total.subtract(net));
        }

        // Group revenue by charge type
        Map<String, BigDecimal> revenueByChargeType = dailyCharges.stream()
                .collect(Collectors.groupingBy(
                        charge -> charge.getChargeType() != null ? charge.getChargeType().getName() : "UNKNOWN",
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                FolioChargeEntity::getTotalAmount,
                                BigDecimal::add
                        )
                ));

        return RevenueReportDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalRevenue(totalRevenue)
                .netRevenue(netRevenue)
                .taxCollected(taxCollected)
                .totalPayments(totalPayments)
                .totalExpenses(totalExpenses)
                .operationalExpenses(operationalExpenses)
                .totalAssets(totalAssets)
                .maintenanceCosts(maintenanceCosts)
                .supplyCosts(supplyCosts)
                .payrollExpenses(payrollExpenses)
                .accountsReceivable(accountsReceivable)
                .accountsPayable(accountsPayable)
                .pettyCash(pettyCash)
                .revenueByChargeType(revenueByChargeType)
                .auditTray(generateAuditTray(dailyCharges, payments, expenses, startDate, endDate))
                .build();
    }


    private DailyRevenueDTO calculateDailyRevenue(
            LocalDate date,
            List<FolioChargeEntity> dailyCharges,
            List<FolioPaymentEntity> payments,
            List<ExpenseEntity> dailyExpenses,
            BigDecimal totalExpenses,
            BigDecimal operationalExpenses,
            BigDecimal totalAssets,
            BigDecimal maintenanceCosts,
            BigDecimal supplyCosts,
            BigDecimal payrollExpenses,
            BigDecimal accountsReceivable,
            BigDecimal accountsPayable,
            BigDecimal pettyCash) {
        BigDecimal totalPayments = payments.stream()
                .map(FolioPaymentEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal netRevenue = BigDecimal.ZERO;
        BigDecimal taxCollected = BigDecimal.ZERO;

        for (FolioChargeEntity charge : dailyCharges) {
            BigDecimal total = charge.getTotalAmount();
            BigDecimal taxPct = charge.getTaxPct();
            
            BigDecimal net;
            if (taxPct != null && taxPct.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal taxFactor = BigDecimal.ONE.add(taxPct.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
                net = total.divide(taxFactor, 2, RoundingMode.HALF_UP);
            } else {
                net = total;
            }

            totalRevenue = totalRevenue.add(total);
            netRevenue = netRevenue.add(net);
            taxCollected = taxCollected.add(total.subtract(net));
        }

        Map<String, BigDecimal> revenueByChargeType = dailyCharges.stream()
                .collect(Collectors.groupingBy(
                        charge -> charge.getChargeType() != null ? charge.getChargeType().getName() : "UNKNOWN",
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                FolioChargeEntity::getTotalAmount,
                                BigDecimal::add
                        )
                ));

        return DailyRevenueDTO.builder()
                .reportDate(date)
                .totalRevenue(totalRevenue)
                .netRevenue(netRevenue)
                .taxCollected(taxCollected)
                .totalPayments(totalPayments)
                .totalExpenses(totalExpenses)
                .operationalExpenses(operationalExpenses)
                .totalAssets(totalAssets)
                .maintenanceCosts(maintenanceCosts)
                .supplyCosts(supplyCosts)
                .payrollExpenses(payrollExpenses)
                .accountsReceivable(accountsReceivable)
                .accountsPayable(accountsPayable)
                .pettyCash(pettyCash)
                .revenueByChargeType(revenueByChargeType)
                .auditTray(generateAuditTray(dailyCharges, payments, dailyExpenses, date, date))
                .build();
    }

    private List<FinancialAuditItemDTO> generateAuditTray(
            List<FolioChargeEntity> charges,
            List<FolioPaymentEntity> payments,
            List<ExpenseEntity> expenses,
            LocalDate start,
            LocalDate end) {
        List<FinancialAuditItemDTO> tray = new ArrayList<>();

        // Add Charges
        for (FolioChargeEntity c : charges) {
            tray.add(FinancialAuditItemDTO.builder()
                .timestamp(c.getChargedAt() != null ? c.getChargedAt() : c.getCreatedAt())
                .type("REVENUE")
                .account(c.getChargeType() != null ? c.getChargeType().getName() : "General Charge")
                .reference("FOLIO #" + c.getFolioId())
                .description(c.getDescription())
                .currency("USD")
                .amount(c.getTotalAmount())
                .status("POSTED")
                .build());
        }

        // Add Payments
        for (FolioPaymentEntity p : payments) {
            String accName = "Payment";
            if (p.getReferenceNumber() != null && !p.getReferenceNumber().isEmpty()) {
                accName = "Payment (" + p.getReferenceNumber() + ")";
            }
            tray.add(FinancialAuditItemDTO.builder()
                .timestamp(p.getRecordedAt() != null ? p.getRecordedAt() : p.getCreatedAt())
                .type("COLLECTION")
                .account(accName)
                .reference("REF #" + p.getReferenceNumber())
                .description("Payment Received")
                .currency(p.getCurrencyCode() != null ? p.getCurrencyCode() : "USD")
                .paymentMethod(p.getPaymentMethod())
                .amount(p.getAmount())
                .status("RECEIVED")
                .build());
        }

        // Add Expenses
        for (ExpenseEntity e : expenses) {
            tray.add(FinancialAuditItemDTO.builder()
                .timestamp(e.getExpenseDate().atStartOfDay())
                .type("EXPENSE")
                .account(e.getExpenseType() != null ? e.getExpenseType().getName() : "General Expense")
                .reference("REF #" + e.getId())
                .description(e.getDescription())
                .currency(e.getCurrencyCode() != null ? e.getCurrencyCode() : "USD")
                .amount(e.getAmount())
                .status("PAID")
                .build());
        }

        // Add Purchase Orders
        List<PurchaseOrderEntity> pos = purchaseOrderRepository.findAllByOrderDateBetween(start, end);
        for (PurchaseOrderEntity po : pos) {
            tray.add(FinancialAuditItemDTO.builder()
                .timestamp(po.getOrderDate().atStartOfDay())
                .type("PROCUREMENT")
                .account("Procurement / Supplies")
                .reference("PO #" + po.getId())
                .description("Order to " + (po.getSupplier() != null ? po.getSupplier().getName() : "Vendor"))
                .amount(BigDecimal.ZERO)
                .status(po.getStatus().toString())
                .build());
        }
        // Add Cash Movements
        List<CashMovementEntity> movements = cashMovementRepository.findAllByMovementDateBetween(start, end);
        for (CashMovementEntity m : movements) {
            // Filter out DRAWING and SAVING as per user request
            if (m.getType() == CashMovementEntity.CashMovementType.DRAWING || 
                m.getType() == CashMovementEntity.CashMovementType.SAVING) {
                continue;
            }
            
            String trayType = "COLLECTION";
            tray.add(FinancialAuditItemDTO.builder()
                .timestamp(m.getMovementDate().atStartOfDay())
                .type(trayType)
                .account(m.getType().toString())
                .reference(m.getType().toString())
                .description(m.getDescription())
                .amount(m.getAmount())
                .status("COMPLETED")
                .build());
        }

        // Sort by timestamp ASC to calculate running balance
        List<FinancialAuditItemDTO> sortedTray = tray.stream()
                .sorted(Comparator.comparing(FinancialAuditItemDTO::getTimestamp))
                .collect(Collectors.toList());

        BigDecimal currentBalance = BigDecimal.ZERO;
        for (FinancialAuditItemDTO item : sortedTray) {
            if ("REVENUE".equals(item.getType()) || "COLLECTION".equals(item.getType())) {
                currentBalance = currentBalance.add(item.getAmount());
            } else if ("EXPENSE".equals(item.getType()) || "PROCUREMENT".equals(item.getType())) {
                currentBalance = currentBalance.subtract(item.getAmount());
            }
            item.setRunningBalance(currentBalance);
        }

        // Return reversed (descending) for display
        Collections.reverse(sortedTray);
        return sortedTray.stream()
                .limit(100)
                .collect(Collectors.toList());
    }

    public List<UserPerformanceDTO> getUserPerformanceReport(LocalDate startDate, LocalDate endDate) {
        Map<Long, UserPerformanceDTO> userStats = new HashMap<>();
        
        java.time.LocalDateTime start = startDate != null ? startDate.atStartOfDay() : LocalDate.now().minusMonths(1).atStartOfDay();
        java.time.LocalDateTime end = endDate != null ? endDate.plusDays(1).atStartOfDay() : LocalDate.now().plusDays(1).atStartOfDay();

        // Fetch check-ins
        List<Map<String, Object>> checkIns = stayRepository.countCheckInsByUserInRange(start, end);
        for (Map<String, Object> map : checkIns) {
            Long userId = ((Number) map.get("userId")).longValue();
            long count = ((Number) map.get("total")).longValue();
            userStats.putIfAbsent(userId, UserPerformanceDTO.builder()
                    .userId(userId)
                    .checkIns(0)
                    .checkOuts(0)
                    .clientsServed(0)
                    .totalCollected(BigDecimal.ZERO)
                    .build());
            userStats.get(userId).setCheckIns(count);
        }

        // Fetch check-outs
        List<Map<String, Object>> checkOuts = stayRepository.countCheckOutsByUserInRange(start, end);
        for (Map<String, Object> map : checkOuts) {
            Long userId = ((Number) map.get("userId")).longValue();
            long count = ((Number) map.get("total")).longValue();
            userStats.putIfAbsent(userId, UserPerformanceDTO.builder()
                    .userId(userId)
                    .checkIns(0)
                    .checkOuts(0)
                    .clientsServed(0)
                    .totalCollected(BigDecimal.ZERO)
                    .build());
            userStats.get(userId).setCheckOuts(count);
        }

        // Fetch collected payments
        List<Map<String, Object>> payments = folioPaymentRepository.sumCollectedByUserInRangeGlobal(start, end);
        for (Map<String, Object> map : payments) {
            Long userId = ((Number) map.get("userId")).longValue();
            BigDecimal total = (BigDecimal) map.get("total");
            userStats.putIfAbsent(userId, UserPerformanceDTO.builder()
                    .userId(userId)
                    .checkIns(0)
                    .checkOuts(0)
                    .clientsServed(0)
                    .totalCollected(BigDecimal.ZERO)
                    .build());
            userStats.get(userId).setTotalCollected(total);
        }

        // Fetch unique folios/clients
        List<Map<String, Object>> clients = folioPaymentRepository.countDistinctFoliosByUserInRangeGlobal(start, end);
        for (Map<String, Object> map : clients) {
            Long userId = ((Number) map.get("userId")).longValue();
            long count = ((Number) map.get("total")).longValue();
            userStats.computeIfPresent(userId, (id, dto) -> {
                dto.setClientsServed(count);
                return dto;
            });
        }

        // Hydrate with user details
        List<UserPerformanceDTO> result = new ArrayList<>(userStats.values());
        for (UserPerformanceDTO dto : result) {
            Optional<UserEntity> userOpt = userRepository.findById(dto.getUserId());
            if (userOpt.isPresent()) {
                dto.setUsername(userOpt.get().getUsername());
                dto.setFullName(userOpt.get().getFullName());
            } else {
                dto.setUsername("Unknown");
                dto.setFullName("Unknown User");
            }
        }

        return result.stream()
                .sorted(Comparator.comparing(UserPerformanceDTO::getTotalCollected).reversed())
                .collect(Collectors.toList());
    }

    public RoomReportDTO getRoomReport(LocalDate startDate, LocalDate endDate) {
        long totalRooms = roomRepository.count(); 
        
        java.time.LocalDateTime start = startDate.atStartOfDay();
        java.time.LocalDateTime end = endDate.plusDays(1).atStartOfDay();

        long occupiedCount = stayRepository.countOccupiedInRange(start, end);
        long checkIns = stayRepository.findAllByStatusIn(List.of(com.hotel_erp.hotel_erp.modules.stay.StayStatus.ACTIVE))
                .stream()
                .filter(s -> s.getDateIn().isAfter(start) && s.getDateIn().isBefore(end))
                .count();
        
        long checkOuts = stayRepository.findAllByStatusIn(List.of(com.hotel_erp.hotel_erp.modules.stay.StayStatus.CHECKED_OUT))
                .stream()
                .filter(s -> s.getDateOut() != null && s.getDateOut().isAfter(start) && s.getDateOut().isBefore(end))
                .count();

        // Total revenue in period
        BigDecimal revenue = folioChargeRepository.findAllByDateRange(start, end)
                .stream()
                .map(FolioChargeEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double occupancyRate = totalRooms > 0 ? (double) occupiedCount / totalRooms * 100 : 0;
        BigDecimal adr = occupiedCount > 0 ? revenue.divide(BigDecimal.valueOf(occupiedCount), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal revPar = totalRooms > 0 ? revenue.divide(BigDecimal.valueOf(totalRooms), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        return RoomReportDTO.builder()
                .totalRooms(totalRooms)
                .occupiedRooms(occupiedCount)
                .availableRooms(Math.max(0, totalRooms - occupiedCount))
                .occupancyRate(occupancyRate)
                .totalRevenue(revenue)
                .adr(adr)
                .revPar(revPar)
                .checkIns(checkIns)
                .checkOuts(checkOuts)
                .build();
    }
}
