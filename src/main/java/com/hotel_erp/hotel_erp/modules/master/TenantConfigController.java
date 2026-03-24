package com.hotel_erp.hotel_erp.modules.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenant")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TenantConfigController {

    @Autowired
    private TenantConfigRepository tenantConfigRepository;

    @GetMapping("/config")
    public ResponseEntity<?> getConfig(@RequestParam String tenant) {
        return tenantConfigRepository.findBySubdomain(tenant)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
