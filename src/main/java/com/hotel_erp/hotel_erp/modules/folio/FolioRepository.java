package com.hotel_erp.hotel_erp.modules.folio;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FolioRepository extends BaseRepository<FolioEntity, Long> {
    Optional<FolioEntity> findByStayId(Long stayId);
}
