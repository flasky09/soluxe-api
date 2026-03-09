package com.hotel_erp.hotel_erp.modules.food;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dining-orders")
@RequiredArgsConstructor
public class DiningOrderController {

    private final DiningOrderService diningOrderService;
    private final DiningOrderMapper diningOrderMapper;

    @GetMapping("/session/{sessionId}")
    public List<DiningOrderDTO> getOrdersBySession(@PathVariable Long sessionId) {
        return diningOrderService.findBySessionId(sessionId).stream()
                .map(diningOrderMapper::toDto)
                .toList();
    }

    @PostMapping
    public DiningOrderDTO createOrder(@RequestBody DiningOrderDTO orderDto) {
        DiningOrderEntity entity = diningOrderMapper.toEntity(orderDto);
        return diningOrderMapper.toDto(diningOrderService.save(entity));
    }

    @GetMapping("/{id}")
    public DiningOrderDTO getOrderById(@PathVariable Long id) {
        return diningOrderService.findById(id)
                .map(diningOrderMapper::toDto)
                .orElseThrow(() -> new RuntimeException("DiningOrder not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        diningOrderService.deleteById(id);
    }
}
