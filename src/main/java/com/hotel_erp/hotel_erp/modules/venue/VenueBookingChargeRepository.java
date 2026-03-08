package com.hotel_erp.hotel_erp.modules.venue;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueBookingChargeRepository extends BaseRepository<VenueBookingChargeEntity, Long> {
    List<VenueBookingChargeEntity> findByVenueBookingId(Long venueBookingId);
}
