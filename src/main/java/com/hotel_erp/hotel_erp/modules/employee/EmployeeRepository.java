package com.hotel_erp.hotel_erp.modules.employee;

import com.hotel_erp.hotel_erp.shared.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface EmployeeRepository extends BaseRepository<EmployeeEntity, Long> {
    @Query("SELECT SUM(e.basicSalary) FROM EmployeeEntity e WHERE e.isActive = true")
    BigDecimal getTotalPayroll();
}
