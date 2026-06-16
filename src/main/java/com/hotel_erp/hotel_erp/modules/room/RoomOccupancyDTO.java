package com.hotel_erp.hotel_erp.modules.room;

import lombok.Data;

@Data
public class RoomOccupancyDTO {
    private Long roomId;
    private String roomNumber;
    private String roomTypeName;
    private String status; // VACANT, OCCUPIED, RESERVED
    
    // Additional occupant info
    private String guestName;
    private Long stayId;
    private Long reservationId;
}
