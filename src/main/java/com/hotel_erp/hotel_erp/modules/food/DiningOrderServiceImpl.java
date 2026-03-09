package com.hotel_erp.hotel_erp.modules.food;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiningOrderServiceImpl implements DiningOrderService {

    private final DiningOrderRepository diningOrderRepository;
    private final DiningSessionRepository diningSessionRepository;

    @Override
    public List<DiningOrderEntity> findBySessionId(Long sessionId) {
        return diningOrderRepository.findBySessionId(sessionId);
    }

    @Override
    public Optional<DiningOrderEntity> findById(Long id) {
        return diningOrderRepository.findById(id);
    }

    @Override
    @Transactional
    public DiningOrderEntity save(DiningOrderEntity order) {
        DiningOrderEntity savedOrder = diningOrderRepository.save(order);
        if (savedOrder.getSession() != null) {
            recalculateSessionTotal(savedOrder.getSession().getId());
        }
        return savedOrder;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        diningOrderRepository.findById(id).ifPresent(order -> {
            Long sessionId = (order.getSession() != null) ? order.getSession().getId() : null;
            diningOrderRepository.deleteById(id);
            if (sessionId != null) {
                recalculateSessionTotal(sessionId);
            }
        });
    }
    
    private void recalculateSessionTotal(Long sessionId) {
        List<DiningOrderEntity> orders = diningOrderRepository.findBySessionId(sessionId);
        BigDecimal total = orders.stream()
                .map(o -> o.getUnitPrice().multiply(BigDecimal.valueOf(o.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        diningSessionRepository.findById(sessionId).ifPresent(session -> {
            session.setTotalAmount(total);
            diningSessionRepository.save(session);
        });
    }
}
