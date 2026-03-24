package com.hotel_erp.hotel_erp.modules.stay;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StayFlagService {

    private final StayRepository repository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void flagStayIfDue(Long stayId, LocalDateTime now) {
        repository.findById(stayId).ifPresent(stay -> {
            if (stay.getStatus() == StayStatus.ACTIVE) {
                if (stay.getDateOut() != null && stay.getDateOut().toLocalDate().isEqual(now.toLocalDate())) {
                    stay.setStatus(StayStatus.DUE_CHECKOUT);
                    repository.save(stay);
                }
            } else if (stay.getStatus() == StayStatus.DUE_CHECKOUT) {
                if (stay.getDateOut() != null && now.isAfter(stay.getDateOut())) {
                    // Normalize to 11:00 AM cutoff for overstay transition
                    if (!stay.getDateOut().toLocalDate().isEqual(now.toLocalDate()) || now.getHour() >= 11) {
                        stay.setStatus(StayStatus.OVERSTAY);
                        repository.save(stay);
                    }
                }
            }
        });
    }
}
