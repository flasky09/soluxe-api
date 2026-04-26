package com.hotel_erp.hotel_erp.modules.report;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
    }

    @Test
    void testGetDailyRevenue() throws Exception {
        DailyRevenueDTO dto = DailyRevenueDTO.builder()
                .reportDate(LocalDate.now())
                .totalRevenue(new BigDecimal("100.00"))
                .netRevenue(new BigDecimal("90.00"))
                .taxCollected(new BigDecimal("10.00"))
                .build();

        when(reportService.getDailyRevenue(any(LocalDate.class))).thenReturn(dto);

        mockMvc.perform(get("/api/reports/daily-revenue")
                        .param("date", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRevenue").value(100.0))
                .andExpect(jsonPath("$.netRevenue").value(90.0));
    }

    @Test
    void testGetRevenueReport() throws Exception {
        RevenueReportDTO dto = RevenueReportDTO.builder()
                .startDate(LocalDate.now().minusDays(1))
                .endDate(LocalDate.now())
                .totalRevenue(new BigDecimal("500.00"))
                .netRevenue(new BigDecimal("450.00"))
                .build();

        when(reportService.getRevenueReport(any(LocalDate.class), any(LocalDate.class))).thenReturn(dto);

        mockMvc.perform(get("/api/reports/revenue-report")
                        .param("startDate", LocalDate.now().minusDays(1).toString())
                        .param("endDate", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRevenue").value(500.0))
                .andExpect(jsonPath("$.netRevenue").value(450.0));
    }
}
