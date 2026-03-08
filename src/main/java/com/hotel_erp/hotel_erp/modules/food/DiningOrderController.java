package com.hotel_erp.hotel_erp.modules.food;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dining-orders")
@RequiredArgsConstructor
public class DiningOrderController {

    private final DiningOrderService diningOrderService;

    @GetMapping("/session/{sessionId}")
    public List<DiningOrderEntity> getOrdersBySession(@PathVariable Long sessionId) {
        return diningOrderService.findBySessionId(sessionId);
    }

    @PostMapping
    public DiningOrderEntity createOrder(@RequestBody DiningOrderEntity order) {
        return diningOrderService.save(order);
    }

    @GetMapping("/{id}")
    public DiningOrderEntity getOrderById(@PathVariable Long id) {
        return diningOrderService.findById(id).orElseThrow(() -> new RuntimeException("DiningOrder not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        diningOrderService.deleteById(id);
    }
}
