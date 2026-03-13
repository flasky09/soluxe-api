package com.hotel_erp.hotel_erp.modules.maintenance;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;
    private final MaintenanceMapper mapper;

    @GetMapping
    public List<MaintenanceDTO> getAllTickets() {
        return maintenanceService.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/room/{roomId}")
    public List<MaintenanceDTO> getTicketsByRoom(@PathVariable Long roomId) {
        return maintenanceService.getTicketsByRoom(roomId);
    }

    @GetMapping("/status/{status}")
    public List<MaintenanceDTO> getTicketsByStatus(@PathVariable MaintenanceStatus status) {
        return maintenanceService.getTicketsByStatus(status);
    }

    @PostMapping
    public MaintenanceDTO createTicket(@RequestBody MaintenanceDTO dto, @RequestParam Long userId) {
        return maintenanceService.reportIssue(dto, userId);
    }

    @PutMapping("/{id}/assign")
    public MaintenanceDTO assignTicket(@PathVariable Long id, @RequestParam Long assigneeId) {
        return maintenanceService.assignTicket(id, assigneeId);
    }

    @PutMapping("/{id}/status")
    public MaintenanceDTO updateStatus(@PathVariable Long id, 
                                       @RequestParam MaintenanceStatus status, 
                                       @RequestParam(required = false) String notes) {
        return maintenanceService.updateStatus(id, status, notes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        maintenanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
