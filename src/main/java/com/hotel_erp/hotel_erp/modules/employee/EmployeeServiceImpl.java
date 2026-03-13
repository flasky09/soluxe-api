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

    public EmployeeServiceImpl(EmployeeRepository repository, 
                               EmployeeMapper mapper) {
        super(repository);
        this.mapper = mapper;
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
    public EmployeeDTO saveEmployee(EmployeeDTO dto) {
        EmployeeEntity entity = mapper.toEntity(dto);
        
        if (dto.getIdType() != null) {
            try {
                entity.setIdType(IdType.valueOf(dto.getIdType()));
            } catch (IllegalArgumentException e) {
                // ignore
            }
        }

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
