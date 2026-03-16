package com.hotel_erp.hotel_erp.shared;

import com.hotel_erp.hotel_erp.modules.employee.DepartmentEntity;
import com.hotel_erp.hotel_erp.modules.employee.DepartmentRepository;
import com.hotel_erp.hotel_erp.modules.folio.PaymentMethodEntity;
import com.hotel_erp.hotel_erp.modules.folio.PaymentMethodRepository;
import com.hotel_erp.hotel_erp.modules.user.Role;
import com.hotel_erp.hotel_erp.modules.user.UserEntity;
import com.hotel_erp.hotel_erp.modules.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PaymentMethodRepository paymentMethodRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedDepartmentAndSuperAdmin();
    }

    private void seedDepartmentAndSuperAdmin() {
        DepartmentEntity adminDept = departmentRepository.findByName("Administration")
                .orElseGet(() -> {
                    DepartmentEntity dept = new DepartmentEntity();
                    dept.setName("Administration");
                    return departmentRepository.save(dept);
                });

        if (userRepository.findByUsername("coresphere").isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setUsername("coresphere");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setFullName("Coresphere Admin");
            admin.setEmail("admin@coresphere.com");
            admin.setPhoneNumber("1234567890");
            admin.setRole(Role.HOTEL_ADMIN);
            admin.setDepartment(adminDept);
            admin.setActive(true);
            userRepository.save(admin);
        }
    }

    private void createPaymentMethod(String name, String description) {
        PaymentMethodEntity method = new PaymentMethodEntity();
        method.setName(name);
        method.setDescription(description);
        method.setActive(true);
        paymentMethodRepository.save(method);
    }
}
