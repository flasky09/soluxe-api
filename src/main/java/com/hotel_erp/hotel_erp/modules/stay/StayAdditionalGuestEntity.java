package com.hotel_erp.hotel_erp.modules.stay;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import com.hotel_erp.hotel_erp.modules.guest.IdTypeEntity;

@Getter
@Setter
@Entity
@Table(name = "stay_additional_guests")
public class StayAdditionalGuestEntity extends BaseEntity {

    @Column(nullable = false)
    private Long stayId;

    @Column(nullable = false)
    private String fullName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_entity_id")
    private IdTypeEntity idType;

    private String idNumber;
}
