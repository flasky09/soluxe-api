package com.hotel_erp.hotel_erp.modules.food;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiningOrderServiceImpl implements DiningOrderService {

    private final DiningOrderRepository diningOrderRepository;

    @Override
    public List<DiningOrderEntity> findBySessionId(Long sessionId) {
        return diningOrderRepository.findBySessionId(sessionId);
    }

    @Override
    public Optional<DiningOrderEntity> findById(Long id) {
        return diningOrderRepository.findById(id);
    }

    @Override
    public DiningOrderEntity save(DiningOrderEntity order) {
        return diningOrderRepository.save(order);
    }

    @Override
    public void deleteById(Long id) {
        diningOrderRepository.deleteById(id);
    }
}
