package com.hotel_erp.hotel_erp.modules.auth;

import com.hotel_erp.hotel_erp.modules.user.UserEntity;
import com.hotel_erp.hotel_erp.modules.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AdminResetController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/force-reset-admin")
    public Map<String, String> resetAdmin() {
        log.info("Force reset admin password triggered");
        
        UserEntity admin = userRepository.findByUsername("admin")
                .orElse(null);
        
        if (admin == null) {
            return Map.of("status", "error", "message", "Admin user not found in database.");
        }
        
        admin.setPasswordHash(passwordEncoder.encode("admin123"));
        admin.setActive(true);
        userRepository.save(admin);
        
        log.info("Admin password has been reset to admin123");
        return Map.of("status", "success", "message", "Admin password has been reset to admin123");
    }
}
