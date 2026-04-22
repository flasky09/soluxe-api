package com.hotel_erp.hotel_erp.modules.food;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import com.hotel_erp.hotel_erp.modules.stay.StayEntity;
import com.hotel_erp.hotel_erp.modules.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dining_sessions")
@EqualsAndHashCode(callSuper = true)
public class DiningSessionEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    private RestaurantTableEntity table;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stay_id")
    private StayEntity stay;

    @Column(name = "guest_name")
    private String guestName;

    @Column(name = "guest_phone")
    private String guestPhone;

    @Column(name = "pax_count")
    private Integer paxCount;

    @Column(name = "split_count")
    private Integer splitCount = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_type")
    private BillingType billingType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "served_by")
    private UserEntity servedBy;

    @Column(name = "opened_at")
    private LocalDateTime openedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SessionStatus status;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;
}
