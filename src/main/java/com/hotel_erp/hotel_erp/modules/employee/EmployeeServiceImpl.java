package com.hotel_erp.hotel_erp.modules.employee;

import com.hotel_erp.hotel_erp.modules.guest.IdType;
import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl extends BaseServiceImpl<EmployeeEntity, Long, EmployeeRepository> implements EmployeeService {
    
    private final EmployeeMapper mapper;
    private final com.hotel_erp.hotel_erp.modules.activity.ActivityLogService activityLogService;

    public EmployeeServiceImpl(EmployeeRepository repository, 
                               EmployeeMapper mapper,
                               com.hotel_erp.hotel_erp.modules.activity.ActivityLogService activityLogService) {
        super(repository);
        this.mapper = mapper;
        this.activityLogService = activityLogService;
    }

    @Override
    public List<EmployeeDTO> findAllEmployees() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EmployeeDTO> findEmployeeById(Long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    @Transactional
    public EmployeeDTO saveEmployee(EmployeeDTO dto, Long userId) {
        EmployeeEntity entity = mapper.toEntity(dto);
        
        if (dto.getIdType() != null) {
            try {
                entity.setIdType(IdType.valueOf(dto.getIdType()));
            } catch (IllegalArgumentException e) {
                // ignore
            }
        }

        if (userId != null) {
            if (entity.getId() == null) {
                entity.setCreatedBy(userId);
            } else {
                entity.setModifiedBy(userId);
            }
        }

        entity = repository.save(entity);
        
        String action = dto.getId() == null ? "CREATE_EMPLOYEE" : "UPDATE_EMPLOYEE";
        activityLogService.logActivity(userId, action, "Saved employee record: " + entity.getFullName());
        
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
