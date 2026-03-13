package com.hotel_erp.hotel_erp.modules.employee;

import com.hotel_erp.hotel_erp.modules.guest.IdTypeRepository;
import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl extends BaseServiceImpl<EmployeeEntity, Long, EmployeeRepository> implements EmployeeService {
    
    private final IdTypeRepository idTypeRepository;
    private final EmployeeMapper mapper;

    public EmployeeServiceImpl(EmployeeRepository repository, 
                               IdTypeRepository idTypeRepository, 
                               EmployeeMapper mapper) {
        super(repository);
        this.idTypeRepository = idTypeRepository;
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
        
        if (dto.getIdTypeId() != null) {
            idTypeRepository.findById(dto.getIdTypeId())
                    .ifPresent(entity::setIdType);
        }

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }
}
