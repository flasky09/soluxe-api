package com.hotel_erp.hotel_erp.modules.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomHistoryItemDTO {
    private String type; // e.g., "STAY", "RESERVATION", "MAINTENANCE", "STATUS_CHANGE"
    private String description;
    private LocalDateTime timestamp;
    private String guestName;
    private String status;
    private String performedBy;
}
