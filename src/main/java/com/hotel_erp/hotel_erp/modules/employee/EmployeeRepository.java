package com.hotel_erp.hotel_erp.modules.employee;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends BaseRepository<EmployeeEntity, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT SUM(e.basicSalary) FROM EmployeeEntity e WHERE e.isActive = true")
    java.math.BigDecimal getTotalPayroll();
}
