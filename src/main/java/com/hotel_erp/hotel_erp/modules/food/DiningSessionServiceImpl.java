package com.hotel_erp.hotel_erp.modules.food;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiningSessionServiceImpl implements DiningSessionService {

    private final DiningSessionRepository diningSessionRepository;

    @Override
    public Optional<DiningSessionEntity> findById(Long id) {
        return diningSessionRepository.findById(id);
    }

    @Override
    public DiningSessionEntity save(DiningSessionEntity session) {
        return diningSessionRepository.save(session);
    }

    @Override
    public void deleteById(Long id) {
        diningSessionRepository.deleteById(id);
    }
}
