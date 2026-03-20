package com.hotel_erp.hotel_erp.modules.folio;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FolioChargeRepository extends BaseRepository<FolioChargeEntity, Long> {
    List<FolioChargeEntity> findAllByFolioId(Long folioId);

    @Query("SELECT COALESCE(SUM(c.totalAmount), 0) FROM FolioChargeEntity c WHERE c.folioId = :folioId")
    java.math.BigDecimal sumTotalByFolioId(@Param("folioId") Long folioId);

    @Query("SELECT f FROM FolioChargeEntity f WHERE f.chargedAt >= :startDate AND f.chargedAt < :endDate")
    List<FolioChargeEntity> findAllByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
