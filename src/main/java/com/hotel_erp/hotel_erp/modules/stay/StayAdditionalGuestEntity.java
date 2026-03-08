package com.hotel_erp.hotel_erp.modules.stay;

import com.hotel_erp.hotel_erp.modules.guest.IdType;
import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "stay_additional_guests")
public class StayAdditionalGuestEntity extends BaseEntity {

    @Column(nullable = false)
    private Long stayId;

    @Column(nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    private IdType idType;

    private String idNumber;
}
