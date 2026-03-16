package com.hotel_erp.hotel_erp.modules.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public List<DepartmentDTO> findAll() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public DepartmentDTO findById(Long id) {
        return departmentRepository.findById(id)
                .map(departmentMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    @Transactional
    public DepartmentDTO save(DepartmentDTO departmentDTO) {
        DepartmentEntity entity = departmentMapper.toEntity(departmentDTO);
        return departmentMapper.toDto(departmentRepository.save(entity));
    }

    @Transactional
    public void deleteById(Long id) {
        try {
            departmentRepository.deleteById(id);
        } catch (Exception e) {
            departmentRepository.findById(id).ifPresent(entity -> {
                entity.setActive(false);
                departmentRepository.save(entity);
            });
        }
    }
}
