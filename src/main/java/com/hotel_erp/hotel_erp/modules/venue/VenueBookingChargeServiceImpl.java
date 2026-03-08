package com.hotel_erp.hotel_erp.modules.venue;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VenueBookingChargeServiceImpl implements VenueBookingChargeService {

    private final VenueBookingChargeRepository venueBookingChargeRepository;

    @Override
    public List<VenueBookingChargeEntity> findByBookingId(Long bookingId) {
        // We will need a findByVenueBookingId or findByBookingId in repository
        return venueBookingChargeRepository.findByVenueBookingId(bookingId);
    }

    @Override
    public Optional<VenueBookingChargeEntity> findById(Long id) {
        return venueBookingChargeRepository.findById(id);
    }

    @Override
    public VenueBookingChargeEntity save(VenueBookingChargeEntity charge) {
        return venueBookingChargeRepository.save(charge);
    }

    @Override
    public void deleteById(Long id) {
        venueBookingChargeRepository.deleteById(id);
    }
}
