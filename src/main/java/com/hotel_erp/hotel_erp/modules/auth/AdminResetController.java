package com.hotel_erp.hotel_erp.modules.auth;

import com.hotel_erp.hotel_erp.modules.user.UserEntity;
import com.hotel_erp.hotel_erp.modules.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminResetController {

    private static final Logger logger = LoggerFactory.getLogger(AdminResetController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * POST /api/admin/reset-password
     *
     * Resets the "coresphere" user password to "admin123".
     * Returns 200 on success, 404 if the coresphere user is not found.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetAdminPassword() {
        logger.warn("Admin password reset endpoint invoked — ensure this is removed after debugging.");

        Optional<UserEntity> adminOpt = userRepository.findByUsername("coresphere");

        if (adminOpt.isEmpty()) {
            logger.error("Admin user not found in the database.");
            return ResponseEntity.status(404).body("Admin user not found.");
        }

        UserEntity admin = adminOpt.get();
        String encoded = passwordEncoder.encode("admin123");
        admin.setPasswordHash(encoded);
        admin.setActive(true);
        userRepository.save(admin);

        logger.info("Admin password successfully reset to 'admin123' for user 'coresphere'.");
        return ResponseEntity.ok("Admin password reset to 'admin123' for user 'coresphere'. Remove this endpoint now.");
    }
}
