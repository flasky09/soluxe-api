package com.hotel_erp.hotel_erp.modules.room;

import com.hotel_erp.hotel_erp.shared.BaseService;
import java.time.LocalDate;
import java.util.List;

public interface RoomService extends BaseService<RoomEntity, Long> {

    RoomHistoryDTO getRoomHistory(Long roomId, LocalDate date);

    List<RoomHistoryItemDTO> getRoomCalendar(Long roomId);

    List<RoomOccupancyDTO> getDailyOccupancy(LocalDate date);

}
