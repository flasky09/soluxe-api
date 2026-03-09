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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final FolioChargeRepository folioChargeRepository;

    public DailyRevenueDTO getDailyRevenue(LocalDate date) {
        // Fetch all charges for the given date
        List<FolioChargeEntity> dailyCharges = folioChargeRepository.findAllByDateRange(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );

        return calculateDailyRevenue(date, dailyCharges);
    }

    public RevenueReportDTO getRevenueReport(LocalDate startDate, LocalDate endDate) {
        // Fetch all charges for the given date range
        List<FolioChargeEntity> dailyCharges = folioChargeRepository.findAllByDateRange(
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay()
        );

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
                .netRevenue(netRevenue)
                .taxCollected(taxCollected)
                .revenueByChargeType(revenueByChargeType)
                .build();
    }

    private DailyRevenueDTO calculateDailyRevenue(LocalDate date, List<FolioChargeEntity> dailyCharges) {
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
                .netRevenue(netRevenue)
                .taxCollected(taxCollected)
                .revenueByChargeType(revenueByChargeType)
                .build();
    }
}
