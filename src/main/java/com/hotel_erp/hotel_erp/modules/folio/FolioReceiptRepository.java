package com.hotel_erp.hotel_erp.modules.folio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolioReceiptRepository extends JpaRepository<FolioReceiptEntity, Long> {
    Optional<FolioReceiptEntity> findByPaymentId(Long paymentId);
    List<FolioReceiptEntity> findAllByFolioId(Long folioId);
    Optional<FolioReceiptEntity> findByReceiptNumber(String receiptNumber);
}
