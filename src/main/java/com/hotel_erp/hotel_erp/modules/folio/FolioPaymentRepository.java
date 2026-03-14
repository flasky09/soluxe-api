package com.hotel_erp.hotel_erp.modules.folio;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FolioPaymentRepository extends JpaRepository<FolioPaymentEntity, Long> {
    List<FolioPaymentEntity> findAllByFolioId(Long folioId);

    @org.springframework.data.jpa.repository.Query("SELECT p FROM FolioPaymentEntity p WHERE p.recordedAt >= :startDate AND p.recordedAt < :endDate")
    List<FolioPaymentEntity> findAllByDateRange(
            @org.springframework.data.repository.query.Param("startDate") java.time.LocalDateTime startDate,
            @org.springframework.data.repository.query.Param("endDate") java.time.LocalDateTime endDate);
}
