package com.hotel_erp.hotel_erp.modules.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomHistoryDTO {
    private Long roomId;
    private String roomNumber;
    private List<RoomHistoryItemDTO> items;
}
