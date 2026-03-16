package com.hotel_erp.hotel_erp.modules.guest;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends BaseRepository<GuestEntity, Long> {
    java.util.List<GuestEntity> findAllByOrderByIdDesc();
}
