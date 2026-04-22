package com.hotel_erp.hotel_erp.modules.stay;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import com.hotel_erp.hotel_erp.modules.guest.IdType;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stay_additional_guests")
@EqualsAndHashCode(callSuper = true)
public class StayAdditionalGuestEntity extends BaseEntity {

    @Column(nullable = false)
    private Long stayId;

    @Column(nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    private IdType idType;

    private String idNumber;
}
