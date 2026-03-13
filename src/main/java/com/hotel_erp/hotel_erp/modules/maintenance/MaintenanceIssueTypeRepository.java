package com.hotel_erp.hotel_erp.modules.maintenance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MaintenanceIssueTypeRepository extends JpaRepository<MaintenanceIssueTypeEntity, Long> {
    Optional<MaintenanceIssueTypeEntity> findByName(String name);
}
