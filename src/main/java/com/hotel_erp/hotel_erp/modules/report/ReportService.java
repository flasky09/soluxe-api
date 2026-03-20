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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
                .reference("FOLIO #" + c.getFolioId())
                .description(c.getDescription())
                .amount(c.getTotalAmount())
                .status("POSTED")
                .build());
        }

        // Add Payments
        for (FolioPaymentEntity p : payments) {
            tray.add(FinancialAuditItemDTO.builder()
                .timestamp(p.getRecordedAt() != null ? p.getRecordedAt() : p.getCreatedAt())
                .type("COLLECTION")
                .reference("REF #" + p.getReferenceNumber())
                .description("Payment Received")
                .amount(p.getAmount())
                .status("RECEIVED")
                .build());
        }

        // Add Expenses
        for (ExpenseEntity e : expenses) {
            tray.add(FinancialAuditItemDTO.builder()
                .timestamp(e.getExpenseDate().atStartOfDay())
                .type("EXPENSE")
                .reference("REF #" + e.getId())
                .description(e.getDescription())
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
}
