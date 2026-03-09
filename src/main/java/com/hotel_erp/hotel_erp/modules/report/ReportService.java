package com.hotel_erp.hotel_erp.modules.report;

import com.hotel_erp.hotel_erp.modules.folio.FolioChargeEntity;
import com.hotel_erp.hotel_erp.modules.folio.FolioChargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final FolioChargeRepository folioChargeRepository;

    public DailyRevenueDTO getDailyRevenue(LocalDate date) {
        // Fetch all charges for the given date (start of day to start of next day)
        // Tenant isolation is handled automatically by Hibernate filters
        List<FolioChargeEntity> dailyCharges = folioChargeRepository.findAllByDateRange(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );

        // Calculate total revenue
        BigDecimal totalRevenue = dailyCharges.stream()
                .map(FolioChargeEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Group revenue by charge type
        Map<String, BigDecimal> revenueByChargeType = dailyCharges.stream()
                .collect(Collectors.groupingBy(
                        charge -> charge.getChargeType().name(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                FolioChargeEntity::getTotalAmount,
                                BigDecimal::add
                        )
                ));

        return DailyRevenueDTO.builder()
                .reportDate(date)
                .totalRevenue(totalRevenue)
                .revenueByChargeType(revenueByChargeType)
                .build();
    }

    public RevenueReportDTO getRevenueReport(LocalDate startDate, LocalDate endDate) {
        // Fetch all charges for the given date range
        List<FolioChargeEntity> dailyCharges = folioChargeRepository.findAllByDateRange(
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay()
        );

        // Calculate total revenue
        BigDecimal totalRevenue = dailyCharges.stream()
                .map(FolioChargeEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Group revenue by charge type
        Map<String, BigDecimal> revenueByChargeType = dailyCharges.stream()
                .collect(Collectors.groupingBy(
                        charge -> charge.getChargeType().name(),
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
                .revenueByChargeType(revenueByChargeType)
                .build();
    }
}
