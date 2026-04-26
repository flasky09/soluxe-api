package com.hotel_erp.hotel_erp.modules.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.hotel_erp.hotel_erp.modules.folio.FolioChargeEntity;
import com.hotel_erp.hotel_erp.modules.folio.FolioChargeRepository;
import com.hotel_erp.hotel_erp.modules.folio.FolioPaymentRepository;
import com.hotel_erp.hotel_erp.modules.folio.FolioRepository;
import com.hotel_erp.hotel_erp.modules.expense.ExpenseEntity;
import com.hotel_erp.hotel_erp.modules.expense.ExpenseTypeEntity;
import com.hotel_erp.hotel_erp.modules.expense.ExpenseRepository;
import com.hotel_erp.hotel_erp.modules.inventory.purchaseorder.PurchaseOrderItemRepository;
import com.hotel_erp.hotel_erp.modules.inventory.purchaseorder.PurchaseOrderRepository;
import com.hotel_erp.hotel_erp.modules.employee.EmployeeRepository;
import com.hotel_erp.hotel_erp.modules.finance.CashMovementRepository;
import com.hotel_erp.hotel_erp.modules.finance.PettyCashRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private FolioChargeRepository folioChargeRepository;
    @Mock
    private FolioPaymentRepository folioPaymentRepository;
    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private PurchaseOrderItemRepository purchaseOrderItemRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;
    @Mock
    private CashMovementRepository cashMovementRepository;
    @Mock
    private PettyCashRepository pettyCashRepository;
    @Mock
    private FolioRepository folioRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    void testGetDailyRevenue_CalculatesTaxesAndTotalsCorrectly() {
        // Arrange
        LocalDate targetDate = LocalDate.now();

        FolioChargeEntity charge1 = new FolioChargeEntity();
        charge1.setTotalAmount(new BigDecimal("105.00")); // gross
        charge1.setTaxPct(new BigDecimal("5.00")); // net = 100.00
        
        when(folioChargeRepository.findAllByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(charge1));
        
        when(folioPaymentRepository.findAllByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());
        
        when(expenseRepository.findAllByExpenseDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of());
                
        // Act
        DailyRevenueDTO dto = reportService.getDailyRevenue(targetDate);
        
        // Assert
        assertEquals(new BigDecimal("105.00"), dto.getTotalRevenue());
        assertEquals(new BigDecimal("100.00"), dto.getNetRevenue().setScale(2));
        assertEquals(new BigDecimal("5.00"), dto.getTaxCollected().setScale(2));
    }

    @Test
    void testGetRevenueReport_AggregatesExpenses() {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(5);
        LocalDate endDate = LocalDate.now();

        ExpenseEntity expense1 = new ExpenseEntity();
        expense1.setAmount(new BigDecimal("200.00"));
        expense1.setExpenseDate(LocalDate.now());
        ExpenseTypeEntity generalType = new ExpenseTypeEntity();
        generalType.setAsset(false);
        expense1.setExpenseType(generalType);
        
        ExpenseEntity expense2 = new ExpenseEntity();
        expense2.setAmount(new BigDecimal("500.00"));
        expense2.setExpenseDate(LocalDate.now());
        ExpenseTypeEntity assetType = new ExpenseTypeEntity();
        assetType.setAsset(true);
        expense2.setExpenseType(assetType);

        when(folioChargeRepository.findAllByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());
        when(folioPaymentRepository.findAllByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());
        when(expenseRepository.findAllByExpenseDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(expense1, expense2));
                
        // Act
        RevenueReportDTO dto = reportService.getRevenueReport(startDate, endDate);
        
        // Assert
        assertEquals(new BigDecimal("700.00"), dto.getTotalExpenses());
        assertEquals(new BigDecimal("200.00"), dto.getOperationalExpenses());
        assertEquals(new BigDecimal("500.00"), dto.getTotalAssets());
    }
}
