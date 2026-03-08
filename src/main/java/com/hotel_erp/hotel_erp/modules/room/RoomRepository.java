package com.hotel_erp.hotel_erp.modules.room;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends BaseRepository<RoomEntity, Long> {
    List<RoomEntity> findAllByStatusIn(List<RoomStatus> statuses);
    List<RoomEntity> findAllByRoomTypeIdAndStatus(Long roomTypeId, RoomStatus status);
}
