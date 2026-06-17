package com.hotel_erp.hotel_erp.modules.room;

import lombok.Data;

@Data
public class RoomOccupancyDTO {
    private Long roomId;
    private String roomNumber;
    private String roomTypeName;
    private String status; // VACANT, OCCUPIED, RESERVED
    private String roomStatus; // Actual room status: AVAILABLE, DIRTY, CLEANING, MAINTENANCE, INSPECTED
    
    // Additional occupant info
    private String guestName;
    private Long stayId;
    private Long reservationId;
}
