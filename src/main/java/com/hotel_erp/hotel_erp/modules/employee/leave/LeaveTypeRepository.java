package com.hotel_erp.hotel_erp.modules.employee.leave;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveTypeEntity, Long> {
    Optional<LeaveTypeEntity> findByName(String name);
}
