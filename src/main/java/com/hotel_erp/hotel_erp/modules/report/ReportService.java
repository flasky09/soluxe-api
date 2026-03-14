package com.hotel_erp.hotel_erp.modules.report;

import com.hotel_erp.hotel_erp.modules.folio.FolioChargeEntity;
import com.hotel_erp.hotel_erp.modules.folio.FolioChargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import com.hotel_erp.hotel_erp.modules.finance.CashMovementRepository;
import com.hotel_erp.hotel_erp.modules.finance.CashMovementEntity;
import com.hotel_erp.hotel_erp.modules.finance.PettyCashRepository;
import com.hotel_erp.hotel_erp.modules.folio.FolioRepository;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final FolioChargeRepository folioChargeRepository;
    private final com.hotel_erp.hotel_erp.modules.folio.FolioPaymentRepository folioPaymentRepository;
    private final com.hotel_erp.hotel_erp.modules.expense.ExpenseRepository expenseRepository;
    private final com.hotel_erp.hotel_erp.modules.inventory.purchaseorder.PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final com.hotel_erp.hotel_erp.modules.employee.EmployeeRepository employeeRepository;
    private final com.hotel_erp.hotel_erp.modules.inventory.InventoryItemRepository inventoryItemRepository;
    private final com.hotel_erp.hotel_erp.modules.inventory.purchaseorder.PurchaseOrderRepository purchaseOrderRepository;
    private final CashMovementRepository cashMovementRepository;
    private final PettyCashRepository pettyCashRepository;
    private final FolioRepository folioRepository;

    public DailyRevenueDTO getDailyRevenue(LocalDate date) {
        // Fetch all charges for the given date
        List<FolioChargeEntity> dailyCharges = folioChargeRepository.findAllByDateRange(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );

        List<com.hotel_erp.hotel_erp.modules.folio.FolioPaymentEntity> payments = folioPaymentRepository.findAllByDateRange(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );

        List<com.hotel_erp.hotel_erp.modules.expense.ExpenseEntity> expenses = expenseRepository.findAllByExpenseDateBetween(date, date);
        BigDecimal totalExpenses = BigDecimal.ZERO;
        BigDecimal operationalExpenses = BigDecimal.ZERO;
        BigDecimal totalAssets = BigDecimal.ZERO;
        BigDecimal maintenanceCosts = BigDecimal.ZERO;

        for (com.hotel_erp.hotel_erp.modules.expense.ExpenseEntity e : expenses) {
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
        if (supplyCosts == null) supplyCosts = BigDecimal.ZERO;

        BigDecimal payrollExpenses = employeeRepository.getTotalPayroll();
        if (payrollExpenses == null) payrollExpenses = BigDecimal.ZERO;

        BigDecimal inventoryValue = inventoryItemRepository.getTotalStockValue();
        if (inventoryValue == null) inventoryValue = BigDecimal.ZERO;

        BigDecimal totalDrawings = cashMovementRepository.getTotalDrawingsInDateRange(date, date);
        if (totalDrawings == null) totalDrawings = BigDecimal.ZERO;

        BigDecimal totalSavings = cashMovementRepository.getTotalSavingsInDateRange(date, date);
        if (totalSavings == null) totalSavings = BigDecimal.ZERO;

        BigDecimal totalCapitalInjected = cashMovementRepository.getTotalCapitalInjectionsInDateRange(date, date);
        if (totalCapitalInjected == null) totalCapitalInjected = BigDecimal.ZERO;

        BigDecimal pettyCash = pettyCashRepository.getTotalPettyCashInDateRange(date, date);
        if (pettyCash == null) pettyCash = BigDecimal.ZERO;

        BigDecimal accountsReceivable = folioRepository.getOutstandingBalanceForClosedFolios();
        if (accountsReceivable == null) accountsReceivable = BigDecimal.ZERO;

        BigDecimal accountsPayable = purchaseOrderRepository.getTotalAccountsPayable();
        if (accountsPayable == null) accountsPayable = BigDecimal.ZERO;

        return calculateDailyRevenue(date, dailyCharges, payments, expenses, totalExpenses, operationalExpenses, totalAssets, maintenanceCosts, supplyCosts, payrollExpenses, inventoryValue, totalDrawings, totalSavings, totalCapitalInjected, accountsReceivable, accountsPayable, pettyCash);
    }

    public RevenueReportDTO getRevenueReport(LocalDate startDate, LocalDate endDate) {
        // Fetch all charges for the given date range
        List<FolioChargeEntity> dailyCharges = folioChargeRepository.findAllByDateRange(
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay()
        );

        List<com.hotel_erp.hotel_erp.modules.folio.FolioPaymentEntity> payments = folioPaymentRepository.findAllByDateRange(
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay()
        );

        BigDecimal totalPayments = payments.stream()
                .map(com.hotel_erp.hotel_erp.modules.folio.FolioPaymentEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<com.hotel_erp.hotel_erp.modules.expense.ExpenseEntity> expenses = expenseRepository.findAllByExpenseDateBetween(startDate, endDate);
        BigDecimal totalExpenses = BigDecimal.ZERO;
        BigDecimal operationalExpenses = BigDecimal.ZERO;
        BigDecimal totalAssets = BigDecimal.ZERO;
        BigDecimal maintenanceCosts = BigDecimal.ZERO;

        for (com.hotel_erp.hotel_erp.modules.expense.ExpenseEntity e : expenses) {
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
        if (supplyCosts == null) supplyCosts = BigDecimal.ZERO;

        BigDecimal payrollExpenses = employeeRepository.getTotalPayroll();
        if (payrollExpenses == null) payrollExpenses = BigDecimal.ZERO;

        BigDecimal inventoryValue = inventoryItemRepository.getTotalStockValue();
        if (inventoryValue == null) inventoryValue = BigDecimal.ZERO;

        BigDecimal totalDrawings = cashMovementRepository.getTotalDrawingsInDateRange(startDate, endDate);
        if (totalDrawings == null) totalDrawings = BigDecimal.ZERO;

        BigDecimal totalSavings = cashMovementRepository.getTotalSavingsInDateRange(startDate, endDate);
        if (totalSavings == null) totalSavings = BigDecimal.ZERO;

        BigDecimal totalCapitalInjected = cashMovementRepository.getTotalCapitalInjectionsInDateRange(startDate, endDate);
        if (totalCapitalInjected == null) totalCapitalInjected = BigDecimal.ZERO;

        BigDecimal pettyCash = pettyCashRepository.getTotalPettyCashInDateRange(startDate, endDate);
        if (pettyCash == null) pettyCash = BigDecimal.ZERO;

        BigDecimal accountsReceivable = folioRepository.getOutstandingBalanceForClosedFolios();
        if (accountsReceivable == null) accountsReceivable = BigDecimal.ZERO;

        BigDecimal accountsPayable = purchaseOrderRepository.getTotalAccountsPayable();
        if (accountsPayable == null) accountsPayable = BigDecimal.ZERO;

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
                .inventoryValue(inventoryValue)
                .totalDrawings(totalDrawings)
                .totalSavings(totalSavings)
                .totalCapitalInjected(totalCapitalInjected)
                .accountsReceivable(accountsReceivable)
                .accountsPayable(accountsPayable)
                .pettyCash(pettyCash)
                .revenueByChargeType(revenueByChargeType)
                .auditTray(generateAuditTray(dailyCharges, payments, expenses, startDate, endDate))
                .build();
    }

    public BalanceSheetDTO getBalanceSheet(LocalDate asOfDate) {
        // Point in time (all time up to asOfDate)
        LocalDate startDate = LocalDate.of(2000, 1, 1); 

        // Current Assets
        BigDecimal collections = folioPaymentRepository.findAllByDateRange(startDate.atStartOfDay(), asOfDate.plusDays(1).atStartOfDay()).stream()
                .map(com.hotel_erp.hotel_erp.modules.folio.FolioPaymentEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal capitalIn = cashMovementRepository.getTotalCapitalInjectionsInDateRange(startDate, asOfDate);
        if (capitalIn == null) capitalIn = BigDecimal.ZERO;

        BigDecimal allExpenses = expenseRepository.findAllByExpenseDateBetween(startDate, asOfDate).stream()
                .map(com.hotel_erp.hotel_erp.modules.expense.ExpenseEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal allDrawings = cashMovementRepository.getTotalDrawingsInDateRange(startDate, asOfDate);
        if (allDrawings == null) allDrawings = BigDecimal.ZERO;

        BigDecimal allPettyCash = pettyCashRepository.getTotalPettyCashInDateRange(startDate, asOfDate);
        if (allPettyCash == null) allPettyCash = BigDecimal.ZERO;

        // Cash on Hand = (Collections + Capital In) - (Expenses + Drawings + Petty Cash)
        BigDecimal cashOnHand = collections.add(capitalIn).subtract(allExpenses).subtract(allDrawings).subtract(allPettyCash);

        BigDecimal accountsReceivable = folioRepository.getOutstandingBalanceForClosedFolios();
        if (accountsReceivable == null) accountsReceivable = BigDecimal.ZERO;

        BigDecimal inventoryValue = inventoryItemRepository.getTotalStockValue();
        if (inventoryValue == null) inventoryValue = BigDecimal.ZERO;

        BigDecimal totalCurrentAssets = cashOnHand.add(accountsReceivable).add(inventoryValue);

        // Fixed Assets
        BigDecimal fixedAssetsValue = expenseRepository.findAllByExpenseDateBetween(startDate, asOfDate).stream()
                .filter(e -> e.getExpenseType() != null && e.getExpenseType().isAsset())
                .map(com.hotel_erp.hotel_erp.modules.expense.ExpenseEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAssets = totalCurrentAssets.add(fixedAssetsValue);

        // Liabilities
        BigDecimal accountsPayable = purchaseOrderRepository.getTotalAccountsPayable();
        if (accountsPayable == null) accountsPayable = BigDecimal.ZERO;

        BigDecimal totalLiabilities = accountsPayable;

        // Equity
        // Net Profit = All Revenue - All OpEx
        BigDecimal totalRevenue = folioChargeRepository.findAllByDateRange(startDate.atStartOfDay(), asOfDate.plusDays(1).atStartOfDay()).stream()
                .map(FolioChargeEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalOpEx = expenseRepository.findAllByExpenseDateBetween(startDate, asOfDate).stream()
                .filter(e -> e.getExpenseType() != null && !e.getExpenseType().isAsset())
                .map(com.hotel_erp.hotel_erp.modules.expense.ExpenseEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal cumulativeNetProfit = totalRevenue.subtract(totalOpEx);
        BigDecimal retainedEarnings = cumulativeNetProfit.subtract(allDrawings);

        BigDecimal totalEquity = capitalIn.add(retainedEarnings);

        return BalanceSheetDTO.builder()
                .asOfDate(asOfDate)
                .cashOnHand(cashOnHand)
                .accountsReceivable(accountsReceivable)
                .inventoryValue(inventoryValue)
                .totalCurrentAssets(totalCurrentAssets)
                .fixedAssetsValue(fixedAssetsValue)
                .totalAssets(totalAssets)
                .accountsPayable(accountsPayable)
                .taxPayable(BigDecimal.ZERO)
                .totalLiabilities(totalLiabilities)
                .capitalInjected(capitalIn)
                .retainedEarnings(retainedEarnings)
                .totalEquity(totalEquity)
                .build();
    }

    public ProfitAndLossDTO getProfitAndLoss(LocalDate startDate, LocalDate endDate) {
        List<FolioChargeEntity> dailyCharges = folioChargeRepository.findAllByDateRange(
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay()
        );

        BigDecimal totalRevenue = dailyCharges.stream()
                .map(FolioChargeEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> revenueByDepartment = dailyCharges.stream()
                .collect(Collectors.groupingBy(
                        charge -> charge.getChargeType() != null ? charge.getChargeType().getName() : "UNASSIGNED",
                        Collectors.reducing(BigDecimal.ZERO, FolioChargeEntity::getTotalAmount, BigDecimal::add)
                ));

        List<com.hotel_erp.hotel_erp.modules.expense.ExpenseEntity> expenses = expenseRepository.findAllByExpenseDateBetween(startDate, endDate);
        
        BigDecimal costOfSales = purchaseOrderItemRepository.getTotalSpendInDateRange(startDate, endDate);
        if (costOfSales == null) costOfSales = BigDecimal.ZERO;

        BigDecimal grossProfit = totalRevenue.subtract(costOfSales);

        BigDecimal payrollExpenses = employeeRepository.getTotalPayroll();
        if (payrollExpenses == null) payrollExpenses = BigDecimal.ZERO;

        BigDecimal maintenanceCosts = BigDecimal.ZERO;
        BigDecimal pettyCashExpenses = pettyCashRepository.getTotalPettyCashInDateRange(startDate, endDate);
        if (pettyCashExpenses == null) pettyCashExpenses = BigDecimal.ZERO;

        BigDecimal operationalExpenses = BigDecimal.ZERO;
        
        for (com.hotel_erp.hotel_erp.modules.expense.ExpenseEntity e : expenses) {
            BigDecimal amt = e.getAmount() != null ? e.getAmount() : BigDecimal.ZERO;
            if (e.getExpenseType() != null && !e.getExpenseType().isAsset()) {
                operationalExpenses = operationalExpenses.add(amt);
                if (e.getExpenseType().getName() != null && e.getExpenseType().getName().toLowerCase().contains("maintenance")) {
                    maintenanceCosts = maintenanceCosts.add(amt);
                }
            }
        }

        BigDecimal totalOperatingExpenses = operationalExpenses.add(payrollExpenses).add(pettyCashExpenses);
        BigDecimal netProfit = grossProfit.subtract(totalOperatingExpenses);

        BigDecimal operatingMargin = totalRevenue.compareTo(BigDecimal.ZERO) > 0 
            ? netProfit.multiply(new BigDecimal("100")).divide(totalRevenue, 2, RoundingMode.HALF_UP) 
            : BigDecimal.ZERO;

        return ProfitAndLossDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalRevenue(totalRevenue)
                .revenueByDepartment(revenueByDepartment)
                .costOfSales(costOfSales)
                .grossProfit(grossProfit)
                .payrollExpenses(payrollExpenses)
                .operationalExpenses(operationalExpenses)
                .maintenanceCosts(maintenanceCosts)
                .pettyCashExpenses(pettyCashExpenses)
                .totalOperatingExpenses(totalOperatingExpenses)
                .netProfit(netProfit)
                .operatingMargin(operatingMargin)
                .build();
    }

    private DailyRevenueDTO calculateDailyRevenue(LocalDate date, List<FolioChargeEntity> dailyCharges, List<com.hotel_erp.hotel_erp.modules.folio.FolioPaymentEntity> payments, List<com.hotel_erp.hotel_erp.modules.expense.ExpenseEntity> dailyExpenses, BigDecimal totalExpenses, BigDecimal operationalExpenses, BigDecimal totalAssets, BigDecimal maintenanceCosts, BigDecimal supplyCosts, BigDecimal payrollExpenses, BigDecimal inventoryValue, BigDecimal totalDrawings, BigDecimal totalSavings, BigDecimal totalCapitalInjected, BigDecimal accountsReceivable, BigDecimal accountsPayable, BigDecimal pettyCash) {
        BigDecimal totalPayments = payments.stream()
                .map(com.hotel_erp.hotel_erp.modules.folio.FolioPaymentEntity::getAmount)
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
                .inventoryValue(inventoryValue)
                .totalDrawings(totalDrawings)
                .totalSavings(totalSavings)
                .totalCapitalInjected(totalCapitalInjected)
                .accountsReceivable(accountsReceivable)
                .accountsPayable(accountsPayable)
                .pettyCash(pettyCash)
                .revenueByChargeType(revenueByChargeType)
                .auditTray(generateAuditTray(dailyCharges, payments, dailyExpenses, date, date))
                .build();
    }

    private List<FinancialAuditItemDTO> generateAuditTray(List<FolioChargeEntity> charges, List<com.hotel_erp.hotel_erp.modules.folio.FolioPaymentEntity> payments, List<com.hotel_erp.hotel_erp.modules.expense.ExpenseEntity> expenses, LocalDate start, LocalDate end) {
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
        for (com.hotel_erp.hotel_erp.modules.folio.FolioPaymentEntity p : payments) {
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
        for (com.hotel_erp.hotel_erp.modules.expense.ExpenseEntity e : expenses) {
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
        List<com.hotel_erp.hotel_erp.modules.inventory.purchaseorder.PurchaseOrderEntity> pos = purchaseOrderRepository.findAllByOrderDateBetween(start, end);
        for (com.hotel_erp.hotel_erp.modules.inventory.purchaseorder.PurchaseOrderEntity po : pos) {
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
            String trayType = "COLLECTION";
            if (m.getType() == CashMovementEntity.CashMovementType.DRAWING) {
                trayType = "EXPENSE";
            }
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
        java.util.Collections.reverse(sortedTray);
        return sortedTray.stream()
                .limit(100)
                .collect(Collectors.toList());
    }
}
