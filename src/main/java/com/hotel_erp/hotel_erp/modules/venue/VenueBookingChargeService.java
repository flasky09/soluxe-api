package com.hotel_erp.hotel_erp.modules.venue;

import java.util.List;
import java.util.Optional;

public interface VenueBookingChargeService {
    List<VenueBookingChargeEntity> findByBookingId(Long bookingId);
    Optional<VenueBookingChargeEntity> findById(Long id);
    VenueBookingChargeEntity save(VenueBookingChargeEntity charge);
    void deleteById(Long id);
}
