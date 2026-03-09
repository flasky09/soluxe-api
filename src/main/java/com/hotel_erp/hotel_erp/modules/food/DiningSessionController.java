package com.hotel_erp.hotel_erp.modules.food;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dining-sessions")
@RequiredArgsConstructor
public class DiningSessionController {

    private final DiningSessionService diningSessionService;
    private final DiningSessionMapper diningSessionMapper;

    @GetMapping
    public List<DiningSessionDTO> getAllSessions() {
        return diningSessionService.findAll().stream()
                .map(diningSessionMapper::toDto)
                .toList();
    }

    @GetMapping("/active")
    public List<DiningSessionDTO> getActiveSessions() {
        return diningSessionService.findActive().stream()
                .map(diningSessionMapper::toDto)
                .toList();
    }

    @PostMapping
    public DiningSessionDTO createSession(@RequestBody DiningSessionDTO sessionDto) {
        DiningSessionEntity entity = diningSessionMapper.toEntity(sessionDto);
        return diningSessionMapper.toDto(diningSessionService.save(entity));
    }

    @GetMapping("/{id}")
    public DiningSessionDTO getSessionById(@PathVariable Long id) {
        return diningSessionService.findById(id)
                .map(diningSessionMapper::toDto)
                .orElseThrow(() -> new RuntimeException("DiningSession not found"));
    }

    @PatchMapping("/{id}/close")
    public DiningSessionDTO closeSession(@PathVariable Long id) {
        return diningSessionMapper.toDto(diningSessionService.closeSession(id));
    }

    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable Long id) {
        diningSessionService.deleteById(id);
    }
}
