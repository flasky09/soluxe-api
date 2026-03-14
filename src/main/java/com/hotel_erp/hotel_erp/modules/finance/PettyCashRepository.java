package com.hotel_erp.hotel_erp.modules.finance;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface PettyCashRepository extends BaseRepository<PettyCashEntity, Long> {
    
    @Query("SELECT SUM(p.amount) FROM PettyCashEntity p WHERE p.expenseDate BETWEEN :start AND :end")
    BigDecimal getTotalPettyCashInDateRange(LocalDate start, LocalDate end);
}
