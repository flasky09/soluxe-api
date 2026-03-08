package com.hotel_erp.hotel_erp.modules.housekeeping;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HousekeepingLogRepository extends BaseRepository<HousekeepingLogEntity, Long> {
    List<HousekeepingLogEntity> findAllByRoomId(Long roomId);
}
