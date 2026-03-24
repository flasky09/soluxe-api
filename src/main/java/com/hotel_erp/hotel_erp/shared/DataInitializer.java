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
    private final com.hotel_erp.hotel_erp.modules.master.TenantConfigRepository tenantConfigRepository;

    public void run(String... args) throws Exception {
        seedTenantConfigs();
        
        java.util.List<com.hotel_erp.hotel_erp.modules.master.TenantConfigEntity> tenants = tenantConfigRepository.findAll();
        for (com.hotel_erp.hotel_erp.modules.master.TenantConfigEntity tenant : tenants) {
            try {
                com.hotel_erp.hotel_erp.config.tenant.TenantContext.setCurrentTenant(tenant.getDbKey());
                seedDepartmentAndSuperAdmin();
            } finally {
                com.hotel_erp.hotel_erp.config.tenant.TenantContext.clear();
            }
        }
    }

    private void seedTenantConfigs() {
        com.hotel_erp.hotel_erp.config.tenant.TenantContext.clear(); // ensure we're NOT in any tenant context
        if (tenantConfigRepository.findBySubdomain("soluxe.mflowpos.com").isEmpty()) {
            com.hotel_erp.hotel_erp.modules.master.TenantConfigEntity soluxe = new com.hotel_erp.hotel_erp.modules.master.TenantConfigEntity();
            soluxe.setSubdomain("soluxe.mflowpos.com");
            soluxe.setDbKey("hotel1");
            soluxe.setHotelName("Soluxe Club Hotel");
            soluxe.setPrimaryColor("#0f172a");
            tenantConfigRepository.save(soluxe);
        }
        if (tenantConfigRepository.findBySubdomain("hotelerp.mflowpos.com").isEmpty()) {
            com.hotel_erp.hotel_erp.modules.master.TenantConfigEntity newHotel = new com.hotel_erp.hotel_erp.modules.master.TenantConfigEntity();
            newHotel.setSubdomain("hotelerp.mflowpos.com");
            newHotel.setDbKey("hotel3");
            newHotel.setHotelName("Hotel ERP Demo");
            newHotel.setPrimaryColor("#059669");
            tenantConfigRepository.save(newHotel);
        }
        if (tenantConfigRepository.findBySubdomain("localhost").isEmpty()) {
            com.hotel_erp.hotel_erp.modules.master.TenantConfigEntity local = new com.hotel_erp.hotel_erp.modules.master.TenantConfigEntity();
            local.setSubdomain("localhost");
            local.setDbKey("hotel1");
            local.setHotelName("Developer Localhost");
            local.setPrimaryColor("#0f172a");
            tenantConfigRepository.save(local);
        }
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
