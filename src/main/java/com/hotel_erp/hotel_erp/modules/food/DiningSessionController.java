package com.hotel_erp.hotel_erp.modules.food;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dining-sessions")
@RequiredArgsConstructor
public class DiningSessionController {

    private final DiningSessionService diningSessionService;

    @PostMapping
    public DiningSessionEntity createSession(@RequestBody DiningSessionEntity session) {
        return diningSessionService.save(session);
    }

    @GetMapping("/{id}")
    public DiningSessionEntity getSessionById(@PathVariable Long id) {
        return diningSessionService.findById(id).orElseThrow(() -> new RuntimeException("DiningSession not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable Long id) {
        diningSessionService.deleteById(id);
    }
}
