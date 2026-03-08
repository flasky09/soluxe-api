package com.hotel_erp.hotel_erp.modules.room;

import lombok.Data;

@Data
public class RoomDTO {
    private Long id;
    private String roomNumber;
    private String floor;
    private Long roomTypeId;
    private String status;
}
