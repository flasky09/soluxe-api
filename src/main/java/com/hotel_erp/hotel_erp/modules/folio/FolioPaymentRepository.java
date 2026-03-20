package com.hotel_erp.hotel_erp.modules.folio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface FolioPaymentRepository extends JpaRepository<FolioPaymentEntity, Long> {
    List<FolioPaymentEntity> findAllByFolioId(Long folioId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM FolioPaymentEntity p WHERE p.folioId = :folioId")
    BigDecimal sumAmountByFolioId(@Param("folioId") Long folioId);

    @Query("SELECT p FROM FolioPaymentEntity p WHERE p.recordedAt >= :startDate AND p.recordedAt < :endDate")
    List<FolioPaymentEntity> findAllByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
