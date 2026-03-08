package com.hotel_erp.hotel_erp.modules.room;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rooms")
public class RoomEntity extends BaseEntity {
    private String roomNumber;
    private String floor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomTypeEntity roomType;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;
}
