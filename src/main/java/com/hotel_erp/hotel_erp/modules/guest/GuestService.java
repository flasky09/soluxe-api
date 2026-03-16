package com.hotel_erp.hotel_erp.modules.guest;

import java.util.List;
import java.util.Optional;

public interface GuestService {
    List<GuestDTO> findAllGuests();
    Optional<GuestDTO> findGuestById(Long id);
    GuestDTO saveGuest(GuestDTO dto);
    void deleteById(Long id);
    void voidGuest(Long id);
}
