package com.hotel_erp.hotel_erp.modules.guest;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GuestDocumentRepository extends BaseRepository<GuestDocumentEntity, Long> {
    List<GuestDocumentEntity> findAllByGuestId(Long guestId);
}
