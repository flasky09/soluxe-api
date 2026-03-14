package com.hotel_erp.hotel_erp.modules.finance;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CashMovementRepository extends BaseRepository<CashMovementEntity, Long> {
    
    List<CashMovementEntity> findAllByMovementDateBetween(LocalDate start, LocalDate end);
    
    @Query("SELECT SUM(c.amount) FROM CashMovementEntity c WHERE c.type = 'DRAWING' AND c.movementDate BETWEEN :start AND :end")
    BigDecimal getTotalDrawingsInDateRange(LocalDate start, LocalDate end);
    
    @Query("SELECT SUM(c.amount) FROM CashMovementEntity c WHERE c.type = 'SAVING' AND c.movementDate BETWEEN :start AND :end")
    BigDecimal getTotalSavingsInDateRange(LocalDate start, LocalDate end);

    @Query("SELECT SUM(c.amount) FROM CashMovementEntity c WHERE c.type = 'CAPITAL_INJECTION' AND c.movementDate BETWEEN :start AND :end")
    BigDecimal getTotalCapitalInjectionsInDateRange(LocalDate start, LocalDate end);
}
