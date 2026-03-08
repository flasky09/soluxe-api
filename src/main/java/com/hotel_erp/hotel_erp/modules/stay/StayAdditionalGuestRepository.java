package com.hotel_erp.hotel_erp.modules.stay;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StayAdditionalGuestRepository extends BaseRepository<StayAdditionalGuestEntity, Long> {
    List<StayAdditionalGuestEntity> findAllByStayId(Long stayId);
}
