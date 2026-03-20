package com.hotel_erp.hotel_erp.modules.employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<EmployeeDTO> findAllEmployees();

    Optional<EmployeeDTO> findEmployeeById(Long id);

    EmployeeDTO saveEmployee(EmployeeDTO dto);

    void deleteById(Long id);

}
