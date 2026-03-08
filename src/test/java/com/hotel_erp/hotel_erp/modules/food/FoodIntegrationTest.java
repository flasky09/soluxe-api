package com.hotel_erp.hotel_erp.modules.food;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FoodIntegrationTest {

    @Autowired
    private DiningSessionService diningSessionService;

    @Autowired
    private DiningOrderService diningOrderService;

    @Test
    void testCreateSessionAndOrder() {
        // Create Dining Session
        DiningSessionEntity session = new DiningSessionEntity();
        session.setGuestName("John Doe");
        session.setOpenedAt(LocalDateTime.now());
        session.setStatus(SessionStatus.OPEN);
        session.setBillingType(BillingType.CHARGE_TO_ROOM);
        session.setTotalAmount(BigDecimal.ZERO);

        DiningSessionEntity savedSession = diningSessionService.save(session);
        assertNotNull(savedSession.getId());

        // Create Dining Order
        DiningOrderEntity order = new DiningOrderEntity();
        order.setSession(savedSession);
        order.setQuantity(2);
        order.setUnitPrice(new BigDecimal("15.50"));
        order.setTotalAmount(new BigDecimal("31.00"));
        order.setStatus(OrderStatus.PREPARING);
        order.setOrderedAt(LocalDateTime.now());

        DiningOrderEntity savedOrder = diningOrderService.save(order);
        assertNotNull(savedOrder.getId());
        assertEquals(savedSession.getId(), savedOrder.getSession().getId());
    }
}
