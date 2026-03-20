package com.hotel_erp.hotel_erp.modules.folio;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface FolioRepository extends BaseRepository<FolioEntity, Long> {

    Optional<FolioEntity> findByStayId(Long stayId);

    Optional<FolioEntity> findByReservationId(Long reservationId);

    Optional<FolioEntity> findByVenueBookingId(Long venueBookingId);

    @Query("SELECT COALESCE(SUM(f.totalAmount), 0) - " +
           "(SELECT COALESCE(SUM(p.amount), 0) FROM FolioPaymentEntity p WHERE p.folioId IN " +
           "(SELECT f2.id FROM FolioEntity f2 WHERE f2.status = 'CLOSED')) " +
           "FROM FolioEntity f WHERE f.status = 'CLOSED'")
    BigDecimal getOutstandingBalanceForClosedFolios();
}
